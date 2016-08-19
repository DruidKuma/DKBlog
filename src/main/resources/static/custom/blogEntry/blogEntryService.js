angular.module("blogApp").service('BlogEntry', ["$http", function($http) {
    return {
        single: function(params) {
            return $http({
                url: BASE_URL + "/api/blog/entry/" + params,
                method: "get"
            })
        },
        page: function(params) {
            return $http({
                url: BASE_URL + "/api/blog/entry/page",
                method: "get",
                params: params
            })
        },
        switchPublishStatus: function(params) {
            return $http({
                url: BASE_URL + "/api/blog/entry/publish/" + params,
                method: "put"
            })
        },
        deletePost: function(params) {
            return $http({
                url: BASE_URL + "/api/blog/entry/" + params,
                method: 'delete'
            })
        },
        generatePermalink: function(blogTitle, id, date) {
            return $http({
                url: BASE_URL + "/api/blog/entry/permalink",
                method: "post",
                data: JSON.stringify({
                    id: id,
                    title: blogTitle,
                    creationDate: date
                })
            })
        },
        savePost: function(blogEntry) {
            return $http({
                url: BASE_URL + "/api/blog/entry",
                method: "post",
                data: blogEntry
            })
        }
    }
}]);