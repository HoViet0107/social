package personal.social.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.social.dto.EditFeedItemRequest;
import personal.social.dto.FeedItemDTO;
import personal.social.enums.ObjectType;
import personal.social.helper.CommonHelpers;
import personal.social.model.Users;
import personal.social.services.FeedItemService;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/feed-items")
public class FeedItemController {
    private final FeedItemService feedItemService;
    private final PagedResourcesAssembler<FeedItemDTO> pagedResourcesAssembler;
    private final CommonHelpers helpers;

    @Autowired
    public FeedItemController(FeedItemService feedItemService, PagedResourcesAssembler<FeedItemDTO> pagedResourcesAssembler, CommonHelpers helpers) {
        this.feedItemService = feedItemService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.helpers = helpers;
    }

    @GetMapping("/posts")
    public PagedModel<?> getAllPosts(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize) throws Exception {
        // use PagedModel for REST-ful API
        Page<FeedItemDTO> posts = feedItemService.getAllFeedItem(pageNumber, pageSize, ObjectType.POST);
        return pagedResourcesAssembler.toModel(posts);
    }

    @GetMapping("{fItemId}")
    public ResponseEntity<?> getFeedItemReactions(@PathVariable Long fItemId){
        return ResponseEntity.ok().body(feedItemService.getFeedItemReactionInfo(fItemId));
    }

    @PostMapping
    public ResponseEntity<?> createFeedItems(
            HttpServletRequest request,
            @RequestBody FeedItemDTO feedItemDTO
    ){
        Users user = helpers.extractToken(request);
        return ResponseEntity.ok().body(feedItemService.createFeedItem(feedItemDTO, user));
    }

    @PutMapping
    public ResponseEntity<?> editFeedItem(
            HttpServletRequest request,
            @RequestBody EditFeedItemRequest editFeedItemRequest
            ) throws AccessDeniedException {
        Users user = helpers.extractToken(request);
        return ResponseEntity.ok().body(feedItemService.editFeedItem(
                editFeedItemRequest.getFeedItemDTO(),
                editFeedItemRequest.getReactionRequest(),
                user)
        );
    }
}
