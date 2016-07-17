angular.module("blogApp").service('BlogEntry', ["$http", function($http) {
    return {
        all: function() {
            return $http({
                url: BASE_URL + "/api/blog/entry",
                method: "get"
            })
        }
    }
}]);