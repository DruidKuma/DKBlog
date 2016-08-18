/**
 * Created by DruidKuma on 8/15/16.
 */
angular.module("blogApp").service('Property', ["$http", function($http) {
    var API_PROPERTY_URL = BASE_URL + "/api/blog/property";

    return {
        all: function() {
            return $http({
                url: API_PROPERTY_URL,
                method: "get"
            })
        },
        save: function(property) {
            return $http({
                url: API_PROPERTY_URL,
                method: "post",
                data: JSON.stringify(property)
            })
        },
        delete: function(property) {
            return $http({
                url: API_PROPERTY_URL + "/" + property.id,
                method: "delete"
            })
        },
        getDefault: function(key) {
            return $http({
                url: API_PROPERTY_URL + "/default/" + key,
                method: "get"
            })
        },
        makeDefault: function(id) {
            return $http({
                url: API_PROPERTY_URL + "/default/" + id,
                method: "put"
            })
        },
        createCopyForCountry: function(property) {
            return $http({
                url: API_PROPERTY_URL + "/copy",
                method: "post",
                data: JSON.stringify(property)
            })
        },
        specificForPanel: function() {
            return $http({
                url: API_PROPERTY_URL + "/specific",
                method: "get"
            })
        }
    }
}]);