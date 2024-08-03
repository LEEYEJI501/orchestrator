package com.example.orchestrator.service.grpc;

import com.example.grpc.SocialServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.grpc.*;

@Service
public class SocialGrpcServiceImpl implements SocialGrpcService {
    private final SocialServiceGrpc.SocialServiceBlockingStub socialServiceBlockingStub;
    private final ManagedChannel channel;

    public SocialGrpcServiceImpl(@Value("${social.grpc.host}") String grpcHost, @Value("${social.grpc.port}") int grpcPort) {
        this.channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
        this.socialServiceBlockingStub = SocialServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public Response followUser(FollowUserRequest followUserRequest) {
        return socialServiceBlockingStub.followUser(followUserRequest);
    }

    @Override
    public Response unfollowUser(UnfollowRequest unfollowRequest) {
        return socialServiceBlockingStub.unfollowUser(unfollowRequest);
    }

    @Override
    public Response getFollowers(GetFollowersRequest getFollowersRequest) {
        return socialServiceBlockingStub.getFollowers(getFollowersRequest);
    }

    @Override
    public Response getPostById(GetPostByIdRequest getPostByIdRequest) {
        return socialServiceBlockingStub.getPostById(getPostByIdRequest);
    }

    @Override
    public Response getPosts(GetPostsRequest getPostsRequest) {
        return socialServiceBlockingStub.getPosts(getPostsRequest);
    }

    @Override
    public Response createPost(CreatePostRequest createPostRequest) {
        return socialServiceBlockingStub.createPost(createPostRequest);
    }

    @Override
    public Response updatePost(UpdatePostRequest updatePostRequest) {
        return socialServiceBlockingStub.updatePost(updatePostRequest);
    }

    @Override
    public Response deletePost(DeletePostRequest deletePostRequest) {
        return socialServiceBlockingStub.deletePost(deletePostRequest);
    }

    @Override
    public Response searchPosts(SearchPostsRequest searchPostsRequest) {
        return socialServiceBlockingStub.searchPosts(searchPostsRequest);
    }

    @Override
    public Response incrementPostLikes(IncrementPostLikesRequest incrementPostLikesRequest) {
        return socialServiceBlockingStub.incrementPostLikes(incrementPostLikesRequest);
    }

    @Override
    public Response decrementPostLikes(DecrementPostLikesRequest decrementPostLikesRequest) {
        return socialServiceBlockingStub.decrementPostLikes(decrementPostLikesRequest);
    }

    @Override
    public Response createComment(CreateCommentRequest createCommentRequest) {
        return socialServiceBlockingStub.createComment(createCommentRequest);
    }

    @Override
    public Response updateComment(UpdateCommentRequest updateCommentRequest) {
        return socialServiceBlockingStub.updateComment(updateCommentRequest);
    }

    @Override
    public Response deleteComment(DeleteCommentRequest deleteCommentRequest) {
        return socialServiceBlockingStub.deleteComment(deleteCommentRequest);
    }

    @Override
    public Response createReply(CreateReplyRequest createReplyRequest) {
        return socialServiceBlockingStub.createReply(createReplyRequest);
    }

    @Override
    public Response incrementCommentLikes(IncrementCommentLikesRequest incrementCommentLikesRequest) {
        return socialServiceBlockingStub.incrementCommentLikes(incrementCommentLikesRequest);
    }

    @Override
    public Response decrementCommentLikes(DecrementCommentLikesRequest decrementCommentLikesRequest) {
        return socialServiceBlockingStub.decrementCommentLikes(decrementCommentLikesRequest);
    }

    @Override
    public Response getUnreadFeedActivities(GetUnreadFeedActivitiesRequest getUnreadFeedActivitiesRequest) {
        return socialServiceBlockingStub.getUnreadFeedActivities(getUnreadFeedActivitiesRequest);
    }

    @Override
    public Response getFeedActivities(GetFeedActivitiesRequest getFeedActivitiesRequest) {
        return socialServiceBlockingStub.getFeedActivities(getFeedActivitiesRequest);
    }

    @Override
    public Response markActivityAsRead(MarkActivityAsReadRequest markActivityAsReadRequest) {
        return socialServiceBlockingStub.markActivityAsRead(markActivityAsReadRequest);
    }

    @Override
    public Response getLatestActivityForFollowees(GetLatestActivityRequest getLatestActivityRequest) {
        return socialServiceBlockingStub.getLatestActivityForFollowees(getLatestActivityRequest);
    }
}
