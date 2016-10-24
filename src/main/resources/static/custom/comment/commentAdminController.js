/**
 * Created by DruidKuma on 10/6/16.
 */
angular.module("blogApp")
    .controller('CommentAdminController',['$scope', 'Comment', '$uibModal', function($scope, Comment, $uibModal) {

        $scope.actionCommentIds = [];
        $scope.comments = [];

        $scope.commentFilter = {
            search: '',
            sort: '',
            typeFilter: '',
            ipFilter: '',
            postFilter: undefined,
            currentPage: 1,
            entriesOnPage: 10,
            totalItems: 0
        };

        $scope.loadComments = function() {
            $scope.loadingProcess = true;
            Comment.loadPageOfComments($scope.commentFilter).then(function(response) {
                $scope.comments = response.data.content;
                $scope.commentFilter.totalItems = response.data.totalElements;
            }, function(error) { $scope.showError() }).finally(function() {
                $scope.loadingProcess = false;
            })
        };
        $scope.resetPaginationAndReload = function() {
            $scope.resetPagination();
            $scope.loadComments();
        };
        $scope.resetPagination = function() {
            $scope.commentFilter.currentPage = 1;
            $scope.commentFilter.totalItems = 0;
        };
        $scope.changeTypeFilter = function(filter) {
            $scope.commentFilter.typeFilter = filter;
            $scope.loadComments();
        };

        $scope.checkIfInActionList = function(commentId) {
            return $scope.actionCommentIds.indexOf(commentId) !== -1;
        };
        $scope.checkIfAllChecked = function() {
            if(!$scope.comments.length) return false;
            return $scope.comments.every(function(comment) {
                return $scope.checkIfInActionList(comment.id);
            });
        };
        $scope.toggleActionCommentId = function(commentId, event) {
            if(event.target.checked) {
                $scope.actionCommentIds.push(commentId);
            }
            else {
                $scope.actionCommentIds.splice($scope.actionCommentIds.indexOf(commentId), 1);
            }
        };

        $scope.toggleAllCheck = function(event) {
            if(event.target.checked) {
                $scope.comments.forEach(function(comment) {
                    if ($scope.actionCommentIds.indexOf(comment.id) == -1) {
                        $scope.actionCommentIds.push(comment.id);
                    }
                });
            }
            else {
                $scope.actionCommentIds = [];
            }
        };

        $scope.filterByIpAddres = function(ipAddress) {
            $scope.commentFilter.ipFilter = ipAddress;
            $scope.resetPaginationAndReload();
        };
        $scope.removeIpFilter = function() {
            $scope.commentFilter.ipFilter = '';
            $scope.resetPaginationAndReload();
        };
        $scope.applyPostFilter = function(postId) {
            $scope.commentFilter.postFilter = postId;
            $scope.resetPaginationAndReload();
        };
        $scope.removePostFilter = function() {
            $scope.commentFilter.postFilter = undefined;
            $scope.resetPaginationAndReload();
        };
        $scope.changeCommentStatus = function(newStatus, commentId) {
            var actionIds;

            if(commentId) actionIds = [commentId];
            else actionIds = $scope.actionCommentIds;

            Comment.changeCommentStatus(actionIds, newStatus).then(function(response) {
                $scope.actionCommentIds = [];
                $scope.loadComments();
            });
        };

        $scope.openCommentEditModal = function(comment) {
            var editCommentModal = $uibModal.open({
                animation: true,
                templateUrl: 'editCommentModal.html',
                controller: 'EditCommentController',
                size: 'lg',
                resolve: {
                    comment: function () {
                        return comment;
                    }
                }
            });

            editCommentModal.result.then(function (editedComment) {
                console.log(editedComment);
                Comment.update(editedComment).then(function(response) {
                    $scope.loadComments();
                }, function(error) { $scope.showError() });
            });
        };

        $scope.loadComments();

    }])
    .controller('EditCommentController', function ($scope, $uibModalInstance, comment) {

        $scope.comment = angular.copy(comment);

        $scope.saveComment = function () {
            $uibModalInstance.close($scope.comment);
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    });