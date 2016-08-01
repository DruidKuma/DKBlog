/**
 * Created by DruidKuma on 7/28/16.
 */
angular.module("blogApp").service('Category', ["$http", function($http) {
    return {
        all: function() {
            return $http({
                url: BASE_URL + "/api/blog/category",
                method: "get"
            })
        },
        one: function(params) {
            return $http({
                url: BASE_URL + "/api/blog/category/" + params,
                method: "get"
            })
        },
        save: function(category) {
            return $http({
                url: BASE_URL + "/api/blog/category",
                method: "post",
                data: JSON.stringify(category)
            })
        }
    }
}]);