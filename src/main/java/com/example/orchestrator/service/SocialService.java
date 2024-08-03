package com.example.orchestrator.service;

import com.example.grpc.*;
import com.example.orchestrator.controller.WebSocketController;
import com.example.orchestrator.dto.*;
import com.example.orchestrator.service.grpc.SocialGrpcService;
import com.example.orchestrator.service.grpc.UserGrpcService;
import com.example.orchestrator.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocialService {
    private final SocialGrpcService socialGrpcService;
    private final UserGrpcService userGrpcService;
    private final WebSocketController webSocketController;
    private final JsonUtil jsonUtil;

    public Long extractUserId(Long id) {
        FindUserRequest request = FindUserRequest.newBuilder()
                .setUserId(id)
                .build();

        Response response = userGrpcService.findUser(request);
        String result = response.getResult();
        Map<String, Object> user = jsonUtil.getMapByKey(result, "user");
        Number userIdNumber = (Number) user.get("id");
        return userIdNumber.longValue();
    }

    public Response followUser(FollowUserDto followUserDto) {
        Long userId = extractUserId(followUserDto.getFolloweeId());

        FollowUserRequest followUserRequest = FollowUserRequest.newBuilder()
                .setFollowerId(followUserDto.getFollowerId())
                .setFolloweeId(userId)
                .build();

        Response response = socialGrpcService.followUser(followUserRequest);
        if (!responseHasError(response)) {
            List<Long> followers = getFollowers(followUserDto.getFolloweeId());
            webSocketController.sendActivityUpdate(followers, this::getLatestActivityForFollowees);
        }
        return response;
    }

    public Response unfollowUser(UnFollowUserDto unFollowUserDto) {
        Long userId = extractUserId(unFollowUserDto.getFolloweeId());

        UnfollowRequest unfollowRequest = UnfollowRequest.newBuilder()
                .setFollowerId(unFollowUserDto.getFollowerId())
                .setFolloweeId(userId)
                .build();

        Response response = socialGrpcService.unfollowUser(unfollowRequest);
        if (!responseHasError(response)) {
            List<Long> followers = getFollowers(unFollowUserDto.getFolloweeId());
            webSocketController.sendActivityUpdate(followers, this::getLatestActivityForFollowees);
        }
        return response;
    }

    public List<Long> getFollowers(Long userId) {
        GetFollowersRequest getFollowersRequest = GetFollowersRequest.newBuilder()
                .setUserId(userId)
                .build();

        Response response = socialGrpcService.getFollowers(getFollowersRequest);
        String result = response.getResult();
        Map<String, Object> followers = jsonUtil.getMapByKey(result, "followers");
        List<Integer> followerIds = (List<Integer>) followers.get("followerIds");

        return followerIds.stream()
                .map(Integer::longValue)
                .collect(Collectors.toList());
    }

    public Response getPostById(Long postId) {
        GetPostByIdRequest getPostByIdRequest = GetPostByIdRequest.newBuilder()
                .setPostId(postId)
                .build();

        return socialGrpcService.getPostById(getPostByIdRequest);
    }

    public Response getPosts(PaginationRequestDto paginationRequest) {
        GetPostsRequest getPostsRequest = GetPostsRequest.newBuilder()
                .setPage(paginationRequest.getPage())
                .setPageSize(paginationRequest.getPageSize())
                .build();

        return socialGrpcService.getPosts(getPostsRequest);
    }

    public Response searchPosts(SearchPostsRequestDto searchPostsRequestDto) {
        SearchPostsRequest searchPostsRequest = SearchPostsRequest.newBuilder()
                .setKeyword(searchPostsRequestDto.getKeyword())
                .setPage(searchPostsRequestDto.getPage())
                .setPageSize(searchPostsRequestDto.getPageSize())
                .build();

        return socialGrpcService.searchPosts(searchPostsRequest);
    }

    public Response createPost(CreatePostDto createPostDto) {
        Long userId = extractUserId(createPostDto.getUserId());

        CreatePostRequest createPostRequest = CreatePostRequest.newBuilder()
                .setUserId(userId)
                .setTitle(createPostDto.getTitle())
                .setAuthor(createPostDto.getAuthor())
                .setAccountName(createPostDto.getAccountName())
                .setContent(createPostDto.getContent())
                .setStockCode(createPostDto.getStockCode())
                .build();

        Response response = socialGrpcService.createPost(createPostRequest);
        if (!responseHasError(response)) {
            List<Long> followers = getFollowers(userId);
            webSocketController.sendActivityUpdate(followers, this::getLatestActivityForFollowees);
        }
        return response;
    }

    public Response updatePost(Long postId, UpdatePostDto updatePostDto) {
        Long userId = extractUserId(updatePostDto.getUserId());

        UpdatePostRequest updatePostRequest = UpdatePostRequest.newBuilder()
                .setPostId(postId)
                .setUserId(userId)
                .setTitle(updatePostDto.getTitle())
                .setContent(updatePostDto.getContent())
                .build();

        Response response = socialGrpcService.updatePost(updatePostRequest);
        if (!responseHasError(response)) {
            List<Long> followers = getFollowers(userId);
            webSocketController.sendActivityUpdate(followers, this::getLatestActivityForFollowees);
        }
        return response;
    }

    public Response deletePost(Long postId, DeletePostDto deletePostDto) {
        Long userId = extractUserId(deletePostDto.getUserId());

        DeletePostRequest deletePostRequest = DeletePostRequest.newBuilder()
                .setPostId(postId)
                .setUserId(userId)
                .build();

        Response response = socialGrpcService.deletePost(deletePostRequest);
        if (!responseHasError(response)) {
            List<Long> followers = getFollowers(userId);
            webSocketController.sendActivityUpdate(followers, this::getLatestActivityForFollowees);
        }
        return response;
    }

    public Response incrementPostLikes(Long postId, LikeDto likeDto) {
        Long userId = extractUserId(likeDto.getUserId());

        IncrementPostLikesRequest incrementPostLikesRequest = IncrementPostLikesRequest.newBuilder()
                .setPostId(postId)
                .setUserId(userId)
                .build();

        Response response = socialGrpcService.incrementPostLikes(incrementPostLikesRequest);
        if (!responseHasError(response)) {
            List<Long> followers = getFollowers(userId);
            webSocketController.sendActivityUpdate(followers, this::getLatestActivityForFollowees);
        }
        return response;
    }

    public Response decrementPostLikes(Long postId, LikeDto likeDto) {
        Long userId = extractUserId(likeDto.getUserId());

        DecrementPostLikesRequest decrementPostLikesRequest = DecrementPostLikesRequest.newBuilder()
                .setPostId(postId)
                .setUserId(userId)
                .build();

        Response response = socialGrpcService.decrementPostLikes(decrementPostLikesRequest);
        if (!responseHasError(response)) {
            List<Long> followers = getFollowers(userId);
            webSocketController.sendActivityUpdate(followers, this::getLatestActivityForFollowees);
        }
        return response;
    }

    public Response createComment(Long postId, CreateCommentDto createCommentDto) {
        Long userId = extractUserId(createCommentDto.getUserId());

        CreateCommentRequest createCommentRequest = CreateCommentRequest.newBuilder()
                .setPostId(postId)
                .setUserId(userId)
                .setContent(createCommentDto.getContent())
                .build();

        Response response = socialGrpcService.createComment(createCommentRequest);
        if (!responseHasError(response)) {
            List<Long> followers = getFollowers(userId);
            webSocketController.sendActivityUpdate(followers, this::getLatestActivityForFollowees);
        }
        return response;
    }

    public Response updateComment(Long commentId, UpdateCommentDto updateCommentDto) {
        Long userId = extractUserId(updateCommentDto.getUserId());

        UpdateCommentRequest updateCommentRequest = UpdateCommentRequest.newBuilder()
                .setCommentId(commentId)
                .setUserId(userId)
                .setContent(updateCommentDto.getContent())
                .build();

        Response response = socialGrpcService.updateComment(updateCommentRequest);
        if (!responseHasError(response)) {
            List<Long> followers = getFollowers(userId);
            webSocketController.sendActivityUpdate(followers, this::getLatestActivityForFollowees);
        }
        return response;
    }

    public Response deleteComment(Long commentId, DeleteCommentDto deleteCommentDto) {
        Long userId = extractUserId(deleteCommentDto.getUserId());

        DeleteCommentRequest deleteCommentRequest = DeleteCommentRequest.newBuilder()
                .setCommentId(commentId)
                .setUserId(userId)
                .build();

        Response response = socialGrpcService.deleteComment(deleteCommentRequest);
        if (!responseHasError(response)) {
            List<Long> followers = getFollowers(userId);
            webSocketController.sendActivityUpdate(followers, this::getLatestActivityForFollowees);
        }
        return response;
    }

    public Response createReply(Long postId, Long parentCommentId, CreateReplyDto createReplyDto) {
        Long userId = extractUserId(createReplyDto.getUserId());

        CreateReplyRequest createReplyRequest = CreateReplyRequest.newBuilder()
                .setPostId(postId)
                .setUserId(userId)
                .setParentCommentId(parentCommentId)
                .setContent(createReplyDto.getContent())
                .build();

        Response response = socialGrpcService.createReply(createReplyRequest);
        if (!responseHasError(response)) {
            List<Long> followers = getFollowers(userId);
            webSocketController.sendActivityUpdate(followers, this::getLatestActivityForFollowees);
        }
        return response;
    }

    public Response incrementCommentLikes(Long commentId, LikeDto likeDto) {
        Long userId = extractUserId(likeDto.getUserId());

        IncrementCommentLikesRequest request = IncrementCommentLikesRequest.newBuilder()
                .setCommentId(commentId)
                .setUserId(userId)
                .build();

        Response response = socialGrpcService.incrementCommentLikes(request);
        if (!responseHasError(response)) {
            List<Long> followers = getFollowers(userId);
            webSocketController.sendActivityUpdate(followers, this::getLatestActivityForFollowees);
        }
        return response;
    }

    public Response decrementCommentLikes(Long commentId, LikeDto likeDto) {
        Long userId = extractUserId(likeDto.getUserId());

        DecrementCommentLikesRequest request = DecrementCommentLikesRequest.newBuilder()
                .setCommentId(commentId)
                .setUserId(userId)
                .build();

        Response response = socialGrpcService.decrementCommentLikes(request);
        if (!responseHasError(response)) {
            List<Long> followers = getFollowers(userId);
            webSocketController.sendActivityUpdate(followers, this::getLatestActivityForFollowees);
        }
        return response;
    }

    public Response getUnreadFeedActivities(Long userId, PaginationRequestDto paginationRequestDto) {
        GetUnreadFeedActivitiesRequest request = GetUnreadFeedActivitiesRequest.newBuilder()
                .setUserId(userId)
                .setPage(paginationRequestDto.getPage())
                .setPageSize(paginationRequestDto.getPageSize())
                .build();

        return socialGrpcService.getUnreadFeedActivities(request);
    }

    public Response getFeedActivities(Long userId, PaginationRequestDto paginationRequestDto) {
        GetFeedActivitiesRequest request = GetFeedActivitiesRequest.newBuilder()
                .setUserId(userId)
                .setPage(paginationRequestDto.getPage())
                .setPageSize(paginationRequestDto.getPageSize())
                .build();

        return socialGrpcService.getFeedActivities(request);
    }

    public Response markActivityAsRead(Long activityId) {
        MarkActivityAsReadRequest request = MarkActivityAsReadRequest.newBuilder()
                .setActivityId(activityId)
                .build();

        return socialGrpcService.markActivityAsRead(request);
    }

    public Response getLatestActivityForFollowees(Long userId) {
        GetLatestActivityRequest request = GetLatestActivityRequest.newBuilder()
                .setUserId(userId)
                .build();

        return socialGrpcService.getLatestActivityForFollowees(request);
    }

    private boolean responseHasError(Response response) {
        String result = response.getResult();
        return result.contains("\"error\"");
    }
}
