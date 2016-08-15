/**
 * Created by DruidKuma on 8/15/16.
 */
angular.module("blogApp").service('Property', ["$http", function($http) {
    return {
        all: function() {
            return $http({
                url: BASE_URL + "/api/blog/property",
                method: "get"
            })
        },
        save: function(property) {
            return $http({
                url: BASE_URL + "/api/blog/property",
                method: "post",
                data: JSON.stringify(property)
            })
        },
        delete: function(property) {
            return $http({
                url: BASE_URL + "/api/blog/property/" + property.id,
                method: "delete"
            })
        }
    }
}]);