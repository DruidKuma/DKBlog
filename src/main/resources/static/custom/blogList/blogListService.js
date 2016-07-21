angular.module("blogApp").service('BlogEntry', ["$http", function($http) {
    return {
        all: function() {
            return $http({
                url: BASE_URL + "/api/blog/entry",
                method: "get"
            })
        },
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
        }
    }
}]);