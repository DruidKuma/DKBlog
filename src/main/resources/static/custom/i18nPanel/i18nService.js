/**
 * Created by DruidKuma on 7/26/16.
 */
angular.module("blogApp").service('I18NService', ["$http", function($http) {

    var API_I18N_URL = BASE_URL + '/api/blog/i18n/';

    return {
        getForKeyAndLang: function(params) {
            return $http({
                url: API_I18N_URL + "get/" + params.key + "/" + params.lang,
                method: "get"
            })
        },
        getTranslationPanelView: function(groupName, targetCountry, filter) {
            return $http({
                url: API_I18N_URL + "panel/" + targetCountry + "/" + groupName,
                method: "get",
                params: filter
            })
        },
        getPageOfTranslations: function(groupName, targetCountry, filter) {
            return $http({
                url: API_I18N_URL + "page/" + targetCountry + "/" + groupName,
                method: "get",
                params:  filter
            })
        },
        saveTranslation: function(translation) {
            return $http({
                url: API_I18N_URL + "save",
                method: "post",
                data: JSON.stringify(translation)
            })
        }
    }
}]);