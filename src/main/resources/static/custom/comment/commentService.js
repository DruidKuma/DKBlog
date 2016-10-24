/**
 * Created by DruidKuma on 7/20/16.
 */
angular.module("blogApp").service('Comment', ["$http", function($http) {
    return {
        allForPost: function(params) {
            return $http({
                url: BASE_URL + "/api/blog/comment/" + params,
                method: "get"
            })
        },
        page: function(params) {
            return $http({
                url: BASE_URL + "/api/blog/comment/page",
                method: "get",
                params: params
            })
        },
        saveComment: function(comment) {
            return $http({
                url: BASE_URL + "/api/blog/comment",
                method: "post",
                data: JSON.stringify(comment)
            })
        },
        loadPageOfComments: function(commentFilter) {
            return $http({
                url: BASE_URL + "/api/blog/comment/page",
                method: "post",
                data: JSON.stringify(commentFilter)
            })
        },
        changeCommentStatus: function(commentIds, newStatus) {
            return $http({
                url: BASE_URL + "/api/blog/comment/status",
                method: "put",
                data: JSON.stringify({commentIds: commentIds, status: newStatus})
            })
        }
    }
}]);