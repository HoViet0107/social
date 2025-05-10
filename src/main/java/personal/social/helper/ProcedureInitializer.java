package personal.social.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ProcedureInitializer implements CommandLineRunner {
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(ProcedureInitializer.class);

    @Autowired
    public ProcedureInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            // Find all SQL files in procedures directory
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources("classpath:procedures/*.sql");
            
            if (resources.length == 0) {
                logger.warn("No SQL procedure files found in procedures directory!");
            }
            
            for (Resource resource : resources) {
                String procedureName = extractProcedureName(resource.getFilename());
                String procedureSQL = readSqlFile(resource);
                
                if (procedureSQL != null && !procedureSQL.isEmpty()) {
                    executeProcedure(procedureName, procedureSQL);
                }
            }
            
            logger.info("All stored procedures created successfully");
        } catch (Exception e) {
            logger.error("Error creating stored procedures: {}", e.getMessage(), e);
        }
    }
    
    private String extractProcedureName(String filename) {
        if (filename == null) return "Unknown";
        // Get procedure name from file name (Ex: GetCommentReplies.sql -> GetTopLevelComments)
        return filename.replace(".sql", "");
    }
    
    private String readSqlFile(Resource resource) {
        StringBuilder sqlBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            boolean hasDelimiter = false;
            
            // First pass: check if file contains DELIMITER
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("DELIMITER")) {
                    hasDelimiter = true;
                    break;
                }
            }
            
            // Reset reader
            reader.close();
            BufferedReader newReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            
            // Second pass: process file based on whether it has DELIMITER or not
            if (hasDelimiter) {
                // Process file with DELIMITER
                boolean insideProcedureBody = false;
                while ((line = newReader.readLine()) != null) {
                    line = line.trim();
                    
                    // Skip empty lines and comments
                    if (line.isEmpty() || line.startsWith("--")) {
                        continue;
                    }
                    
                    // Handle DELIMITER lines
                    if (line.startsWith("DELIMITER")) {
                        if (line.equals("DELIMITER ;")) {
                            insideProcedureBody = false;
                        } else if (line.equals("DELIMITER //")) {
                            insideProcedureBody = true;
                        }
                        continue;
                    }
                    
                    // Handle procedure body
                    if (insideProcedureBody && line.endsWith("//")) {
                        line = line.substring(0, line.length() - 2) + ";";
                    }
                    
                    sqlBuilder.append(line).append("\n");
                }
            } else {
                // Process file without DELIMITER
                while ((line = newReader.readLine()) != null) {
                    sqlBuilder.append(line).append("\n");
                }
            }
            
            return sqlBuilder.toString();
        } catch (IOException e) {
            logger.error("Error reading SQL file: {}", e.getMessage(), e);
            return null;
        }
    }
    
    private void executeProcedure(String procedureName, String procedureSQL) {
        // Find actual procedure name from SQL
        Pattern pattern = Pattern.compile("CREATE\\s+PROCEDURE\\s+(\\w+)\\s*\\(", 
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(procedureSQL);
        String actualProcedureName = matcher.find() ? matcher.group(1) : procedureName;
        
        try (Connection connection = dataSource.getConnection()) {
            try (Statement stmt = connection.createStatement()) {
                // Delete old procedure if it exists
                stmt.execute("DROP PROCEDURE IF EXISTS " + actualProcedureName);
                logger.info("Dropped existing procedure: {}", actualProcedureName);
            }
            
            try (Statement stmt = connection.createStatement()) {
                // Create new procedure
                stmt.execute(procedureSQL);
                logger.info("Created procedure: {}", actualProcedureName);
            }
        } catch (SQLException e) {
            logger.error("Error executing procedure {}: {}", actualProcedureName, e.getMessage(), e);
        }
    }
}
