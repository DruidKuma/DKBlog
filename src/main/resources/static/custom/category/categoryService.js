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
        simple: function() {
            return $http({
                url: BASE_URL + "/api/blog/category/simple",
                method: "get"
            })
        },
        forEntryEdit: function() {
            return $http({
                url: BASE_URL + "/api/blog/category/entry/edit",
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
        },
        removeFromCountry: function(categoryId) {
            return $http({
                url: BASE_URL + "/api/blog/category/country/" + categoryId,
                method: "delete"
            })
        },
        remove: function(categoryId) {
            return $http({
                url: BASE_URL + "/api/blog/category/" + categoryId,
                method: "delete"
            })
        }
    }
}]);