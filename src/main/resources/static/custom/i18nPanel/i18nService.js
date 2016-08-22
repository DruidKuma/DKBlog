/**
 * Created by DruidKuma on 7/26/16.
 */
angular.module("blogApp").service('I18NService', ["$http", function($http) {
    return {
        getForKeyAndLang: function(params) {
            return $http({
                url: BASE_URL + "/api/blog/i18n/get/" + params.key + "/" + params.lang,
                method: "get"
            })
        },
        getTranslationPanelView: function(groupName, targetCountry) {
            return $http({
                url: BASE_URL + "/api/blog/i18n/panel/" + targetCountry + "/" + groupName,
                method: "get"
            })
        }
    }
}]);