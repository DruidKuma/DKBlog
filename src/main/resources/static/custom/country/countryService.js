angular.module("blogApp").service('Country', ["$http", function($http) {
    return {
        flags: function() {
            return $http({
                url: BASE_URL + "/api/blog/country",
                method: "get"
            })
        }
    }
}]);