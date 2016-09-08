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
        },
        saveTranslationGroup: function(translationGroupKey, parentGroup) {
            return $http({
                url: API_I18N_URL + "group/save",
                method: "post",
                data: JSON.stringify({group: parentGroup, key: translationGroupKey})
            })
        },
        deleteGroup: function(groupName) {
            return $http({
                url: API_I18N_URL + "group/remove/" + groupName,
                method: "delete"
            })
        },
        deleteTranslation: function(groupNameKey, translationKey) {
            return $http({
                url: API_I18N_URL + "translation/remove/" + groupNameKey + "/" + translationKey,
                method: "delete"
            })
        },
        clearTranslationsForCurrentCountry: function(country) {
            return $http({
                url: API_I18N_URL + "translation/remove/country/" + country.isoCode,
                method: "delete"
            })
        },
        exportWithCustomConfig: function(config) {
            return $http({
                url: API_I18N_URL + "export/custom",
                method: "post",
                data: JSON.stringify(config),
                responseType: 'arraybuffer'
            })
        },
        translateViaExternalApi: function(config) {
            return $http({
                url: API_I18N_URL + "translate/external",
                method: "post",
                data: JSON.stringify(config)
            })
        },
        loadCountryConfig: function(countryIso) {
            return $http({
                url: API_I18N_URL + "data/country/" + countryIso,
                method: "get"
            })
        },
        loadAllCountryNames: function() {
            return $http({
                url: API_I18N_URL + "data/names/country",
                method: "get"
            })
        },
        toggleCountryEnabled: function(isoCode) {
            return $http({
                url: API_I18N_URL + "data/country/enabled/" + isoCode,
                method: "post"
            })
        },
        changeDefaultLanguage: function(country) {
            return $http({
                url: API_I18N_URL + "data/country/language/default/" + country.isoCode,
                method: "post",
                data: JSON.stringify(country.defaultLanguage)
            })
        }
    }
}]);