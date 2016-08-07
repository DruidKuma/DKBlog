/**
 * Created by DruidKuma on 8/2/16.
 */
angular.module("blogApp").service('Media', ["$http", function($http) {
    return {
        all: function() {
            return $http({
                url: BASE_URL + "/api/blog/media",
                method: "get"
            })
        }
    }
}]);