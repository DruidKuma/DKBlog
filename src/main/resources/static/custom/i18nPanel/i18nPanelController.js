/**
 * Created by DruidKuma on 7/26/16.
 */
angular.module("blogApp")
    .controller('I18nPanelController',['$scope', 'I18NService', function($scope, I18NService) {
        //Page Heading
        $scope.$on('$routeChangeSuccess', function () {
            $scope.pageHeading.title = "I18N Panel";
        });
        $scope.targetCountry = $scope.currentCountry;
        $scope.translationLoading = false;


        $scope.chosenGroupParts = [];

        $scope.loadPanelView = function() {
            $scope.translationLoading = true;
            I18NService.getTranslationPanelView($scope.chosenGroupParts.join('.'), $scope.targetCountry.isoCode).then(function(response) {
                $scope.currentGroupView = response.data;
                $scope.translationLoading = false;
            }, function(error) { $scope.showError() });
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