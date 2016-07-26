/**
 * Created by DruidKuma on 7/26/16.
 */
angular.module("blogApp").service('I18NService', ["$http", function($http) {
    return {
        all: function() {
            return $http({
                url: BASE_URL + "/#",
                method: "get"
            })
        },
        groupNames: function(params) {
            return $http({
                url: BASE_URL + "/api/blog/i18n/panel/names/" + params,
                method: "get"
            });
        }
    }
}]);