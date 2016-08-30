/**
 * Created by DruidKuma on 7/26/16.
 */
angular.module("blogApp")
    .controller('I18nPanelController',['$scope', 'I18NService', '$translate', '$translatePartialLoader', '$uibModal', '$window', 'Upload', function($scope, I18NService, $translate, $translatePartialLoader, $uibModal, $window, Upload) {
        //Page Heading
        $scope.$on('$routeChangeSuccess', function () {
            $scope.pageHeading.title = "i18nPanel";
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
            entriesOnPage: 10,
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
                $translatePartialLoader.deleteAll();
                $translatePartialLoader.addPart('layout');
                $translate.refresh();
            }, function(error) { $scope.showError() })
        };

        $scope.removeTranslation = function(translation) {
            $scope.executeWithWarning("Are you sure?",
                "This action will delete this translation key with all translations and can influence the work of the whole application!",
                "Yes, delete it!",
                function() {
                    I18NService.deleteTranslation($scope.chosenGroupParts.join('.'), translation.key).then(function(response) {
                        $scope.loadTranslations();
                    }, function(error) { $scope.showError() });
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

        $scope.newGroupFormRequested = false;
        $scope.newCategoryNameKey = '';
        $scope.showNewGroupForm = function() {
            $scope.newGroupFormRequested = true;
        };

        $scope.saveTranslationGroup = function() {
            I18NService.saveTranslationGroup($scope.newCategoryNameKey, $scope.chosenGroupParts.join('.')).then(function(response) {
                $scope.newGroupFormRequested = false;
                $scope.chosenGroupParts.push($scope.newCategoryNameKey);
                $scope.newCategoryNameKey = '';
                $scope.loadPanelView();
            }, function(error) { $scope.showError() });
        };

        $scope.deleteCurrentGroup = function() {
            $scope.executeWithWarning("Are you sure?",
                "This action will delete this group with all translations and can influence work of the whole application!",
                "Yes, delete it!",
                function() {
                    I18NService.deleteGroup($scope.chosenGroupParts.join('.')).then(function(response){
                        $scope.chosenGroupParts = [];
                        $scope.loadPanelView();
                    });
                }, function(error) { $scope.showError() });
        };

        $scope.clearTranslationsForCurrentCountry = function() {
            I18NService.clearTranslationsForCurrentCountry().then(function(response) {
                $scope.loadPanelView();
            }, function(error) { $scope.showError() })
        };

        $scope.openCustomExporterModal = function() {
            var exportModal = $uibModal.open({
                animation: true,
                templateUrl: 'customExportModal.html',
                controller: 'CustomExportController',
                size: 'lg'
            });

            exportModal.result.then(function (exportConfig) {
                I18NService.exportWithCustomConfig({
                    targetCountry: $scope.targetCountry.isoCode,
                    groupName: $scope.chosenGroupParts.join('.'),
                    columnSeparator: exportConfig.columnSeparator,
                    rowSeparator: exportConfig.rowSeparator
                }).then(function(response) {
                    var blob = new Blob([response.data], {type: "text/plain"});
                    var objectUrl = URL.createObjectURL(blob);
                    $window.open(objectUrl, '_self');
                }, function() { $scope.showError() })
            });
        };

        $scope.openUploadModal = function() {
            var uploadModal = $uibModal.open({
                animation: true,
                templateUrl: 'uploadModal.html',
                controller: 'UploadTranslationsController',
                size: 'lg',
                resolve: {
                    Upload: function() {
                        return Upload;
                    }
                }
            });

            uploadModal.result.then(function (response) {
                $scope.loadPanelView();
            });
        };

        $scope.loadPanelView();

    }])
    .controller('CustomExportController', function ($scope, $uibModalInstance) {

        $scope.columnSeparator = ',';
        $scope.rowSeparator = '';

        $scope.exportTranslations = function () {
            $uibModalInstance.close({columnSeparator: $scope.columnSeparator, rowSeparator: $scope.rowSeparator});
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    })
    .controller('UploadTranslationsController', function ($scope, $uibModalInstance, Upload) {

        $scope.acceptFormat = 'application/json';
        $scope.uploadError = false;
        $scope.uploadErrorMessage = '';

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.uploadTranslations = function(file, errFiles) {
            if (file) {
                $scope.uploadError = false;
                $scope.uploadErrorMessage = '';
                Upload.upload({
                    url: BASE_URL + '/api/blog/i18n/import/json',
                    data: {file: file}
                }).then(function (response) {
                    $uibModalInstance.close(response);
                }, function (errResponse) {
                    $scope.uploadError = true;
                    $scope.uploadErrorMessage = errResponse.data.message;
                });
            }
        }
    });