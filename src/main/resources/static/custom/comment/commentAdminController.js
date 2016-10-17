/**
 * Created by DruidKuma on 10/6/16.
 */
angular.module("blogApp")
    .controller('CommentAdminController',['$scope', 'Comment', function($scope, Comment) {

        $scope.actionCommentIds = [];
        $scope.comments = [
            {
                id: 1,
                author: 'Iurii Miedviediev',
                email: 'druidkuma@gmail.com',
                ipAddress: '10.63.0.33',
                date: 999303004098,
                text: 'Events. Backrounds to become issues in a dream atmosphere elf, Dwarf, Personal, Draconian, Troll, Ogre, orc, Massive, Satanic force, Angel perhaps what with the these sort of. Dwarf, Gnome, Your, Elf and / or troll are viewed as meant for good hands per hour. Precious Carmichael: Aren’t getting you intending. Video wows is extremely enslaving',
                type: 'APPROVED',
                postId: 1625,
                totalCommentsForPost: 18
            },
            {
                id: 2,
                author: 'Iurii Miedviediev',
                email: 'druidkuma@gmail.com',
                ipAddress: '10.63.0.33',
                date: 999303004098,
                text: 'Events. Backrounds to become issues in a dream atmosphere elf, Dwarf, Personal, Draconian, Troll, Ogre, orc, Massive, Satanic force, Angel perhaps what with the these sort of. Dwarf, Gnome, Your, Elf and / or troll are viewed as meant for good hands per hour. Precious Carmichael: Aren’t getting you intending. Video wows is extremely enslaving',
                type: 'APPROVED',
                postId: 1625,
                totalCommentsForPost: 777
            }
        ];

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
                //$scope.comments = response.data;
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

        $scope.loadComments();

    }]);