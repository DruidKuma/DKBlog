/**
 * Created by DruidKuma on 7/26/16.
 */
angular.module("blogApp")
    .controller('I18nPanelController',['$scope', 'I18NService', function($scope, I18NService) {
        //Page Heading
        $scope.$on('$routeChangeSuccess', function () {
            $scope.pageHeading.title = "I18N Panel";
        });
        $scope.$on('countryChanged', function(event, data) {
            $scope.loadPanelView();
        });
        $scope.targetCountry = $scope.currentCountry;
        $scope.translationLoading = false;


        $scope.chosenGroupParts = [];
        $scope.translationsFilter = {
            search: '',
            sort: '',
            currentPage: 1,
            entriesOnPage: 2,
            totalItems: 0
        };

        $scope.changeTranslationsSort = function(sortField) {
            if($scope.translationsFilter.sort == sortField + ' ASC') $scope.translationsFilter.sort = sortField + ' DESC';
            else if ($scope.translationsFilter.sort == sortField + ' DESC') $scope.translationsFilter.sort = '';
            else $scope.translationsFilter.sort = sortField + ' ASC';
            $scope.loadTranslations();
        };

        $scope.loadPanelView = function() {
            $scope.translationsFilter.sort = '';
            $scope.translationsFilter.search = '';
            $scope.translationsFilter.currentPage = 1;
            $scope.translationsFilter.totalItems = 0;

            $scope.translationLoading = true;
            I18NService.getTranslationPanelView($scope.chosenGroupParts.join('.'), $scope.targetCountry.isoCode, $scope.translationsFilter).then(function(response) {
                $scope.currentGroupView = angular.copy(response.data);
                $scope.currentGroupView.translations = response.data.translations.content;
                $scope.translationsFilter.totalItems = response.data.translations.totalElements;
                $scope.translationLoading = false;
            }, function(error) { $scope.showError() });
        };

        $scope.loadTranslations = function() {
            I18NService.getPageOfTranslations($scope.chosenGroupParts.join('.'), $scope.targetCountry.isoCode, $scope.translationsFilter).then(function(response) {
                $scope.currentGroupView.translations = response.data.content;
                $scope.translationsFilter.totalItems = response.data.totalElements;
            }, function(error) { $scope.showError() })
        };

        $scope.changeTargetCountry = function(country) {
            $scope.targetCountry = country;
            $scope.loadPanelView();
        };

        $scope.resetFilter = function() {
            $scope.chosenGroupParts = [];
            $scope.loadPanelView();
        };

        $scope.removeGroupParts = function(index) {
            $scope.chosenGroupParts.splice(index+1, $scope.chosenGroupParts.length);
            $scope.loadPanelView();
        };

        $scope.openGroup = function(group) {
            $scope.chosenGroupParts.push(group);
            $scope.loadPanelView();
        };

        $scope.saveTranslation = function(data, translation) {
            I18NService.saveTranslation({
                group: $scope.chosenGroupParts.join('.'),
                key: data.key,
                value: data.target,
                countryIso: $scope.targetCountry.isoCode
            }).then(function(response) {
                $scope.loadTranslations();
            }, function(error) { $scope.showError() })
        };

        $scope.removeTranslation = function(translation) {
            $scope.executeWithWarning("Are you sure?",
                "This action will delete this translation key with all translations and can influence the work of the whole application!",
                "Yes, delete it!",
                function() {
                    console.log('Implement me');
                });
        };

        $scope.addNewTranslation = function() {
            $scope.inserted = {
                key: '',
                src: '',
                target: '',
                lastModified: new Date(),
                new: true
            };
            $scope.currentGroupView.translations.push($scope.inserted);
        };

        $scope.cancelFormEditing = function(form) {
            form.$cancel();
            if($scope.inserted) {
                $scope.currentGroupView.translations.pop();
            }
            delete $scope.inserted;
        };

        $scope.loadPanelView();
    }]);