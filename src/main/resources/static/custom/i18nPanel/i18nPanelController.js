/**
 * Created by DruidKuma on 7/26/16.
 */
angular.module("blogApp")
    .controller('I18nPanelController',['$scope', 'I18NService', '$translate', '$translatePartialLoader', '$uibModal', '$window', 'Upload', 'FileSaver', 'toaster', '$filter', function($scope, I18NService, $translate, $translatePartialLoader, $uibModal, $window, Upload, FileSaver, toaster, $filter) {
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
        $scope.chosenConfigCountry = $scope.currentCountry.isoCode;

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
            I18NService.clearTranslationsForCurrentCountry($scope.targetCountry).then(function(response) {
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
                    var data = new Blob([response.data], {type: "text/plain;charset=utf-8"});
                    FileSaver.saveAs(data, 'translations.txt');
                }, function() { $scope.showError() })
            });
        };

        $scope.openUploadModal = function(format) {
            var uploadModal = $uibModal.open({
                animation: true,
                templateUrl: 'uploadModal.html',
                controller: 'UploadTranslationsController',
                size: 'lg',
                resolve: {
                    Upload: function() {
                        return Upload;
                    },
                    format: function() {
                        return format;
                    }
                }
            });

            uploadModal.result.then(function (response) {
                $scope.loadPanelView();
            });
        };

        $scope.openExternalApiTranslationModal = function(type) {
            var translationModal = $uibModal.open({
                animation: true,
                templateUrl: 'externalTranslation.html',
                controller: 'ExternalTranslationsController',
                size: 'lg',
                resolve: {
                    config: function() {
                        return {
                            type: type,
                            srcCountry: $scope.currentCountry,
                            destCountry: $scope.targetCountry,
                            group: $scope.chosenGroupParts.join('.')
                        }
                    },
                    I18NService: function() {
                        return I18NService;
                    }
                }
            });

            translationModal.result.then(function (response) {
                toaster.pop({
                    type: 'success',
                    title: 'Translation Completed',
                    body: 'Translation via external API process was successfully completed',
                    showDuration: 3000,
                    timeout: 3000
                });
                $scope.loadTranslations();
            });
        };

        $scope.loadCountryConfigData = function() {
            I18NService.loadCountryConfig($scope.chosenConfigCountry).then(function(response) {
                $scope.configEditCountry = response.data;
                $scope.loadLanguageVariants();
            }, function(error) { $scope.showError() })
        };

        $scope.initCountryConfigData = function() {
            I18NService.loadAllCountryNames().then(function(response) {
                $scope.countryData = response.data;
                $scope.loadCountryConfigData();
            }, function(error) { $scope.showError() })
        };

        $scope.toggleCountryDataEnabled = function(country) {
            country.enabled = !country.enabled;
            I18NService.toggleCountryEnabled(country.isoCode)
                .then(function(response) {}, function(error) { $scope.showError() })
        };

        $scope.changeDefaultLanguageForCountry = function() {
            I18NService.changeDefaultLanguage($scope.configEditCountry)
                .then(function(response) { $scope.loadLanguageVariants() }, function(error) { $scope.showError() })
        };

        $scope.loadLanguageVariants = function() {
            I18NService.loadLangVariants().then(function(response) {
                $scope.allLanguages = response.data;
                angular.forEach($scope.allLanguages, function(lang) {
                    var found = $filter('filter')($scope.configEditCountry.languages, {isoCode: lang.isoCode}, true);
                    if(found.length) {
                        lang.ticked = true;
                        if($scope.configEditCountry.defaultLanguage.isoCode == lang.isoCode) lang.disabled = true;
                    }
                });
            }, function(error) { $scope.showError() })
        };

        $scope.updateCountryLanguages = function() {
            I18NService.updateCountryLanguages($scope.configEditCountry)
                .then(function(response) {},
                    function(error) { $scope.showError(error.data.message) });
        };


        $scope.loadPanelView();
        $scope.initCountryConfigData();
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
    .controller('UploadTranslationsController', function ($scope, $uibModalInstance, Upload, format) {

        $scope.formats = {
            json: 'application/json',
            excel: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
            custom: 'text/plain, text/csv, text/tsv'
        };

        $scope.format = format;
        $scope.acceptFormat = $scope.formats[$scope.format];
        $scope.uploadError = false;
        $scope.uploadErrorMessage = '';
        $scope.columnSeparator = ',';
        $scope.rowSeparator = ';';

        $scope.uploadTranslations = function(file, errFiles) {
            if (file) {
                $scope.uploadError = false;
                $scope.uploadErrorMessage = '';
                Upload.upload({
                    url: BASE_URL + '/api/blog/i18n/import/' + $scope.format,
                    data: {
                        file: file,
                        columnSeparator: $scope.columnSeparator,
                        rowSeparator: $scope.rowSeparator
                    }
                }).then(function (response) {
                    $uibModalInstance.close(response);
                }, function (errResponse) {
                    $scope.uploadError = true;
                    $scope.uploadErrorMessage = errResponse.data.message;
                });
            }
        }
    })
    .controller('ExternalTranslationsController', function ($scope, $uibModalInstance, config, I18NService) {

        $scope.config = config;
        $scope.config.override = 'true';
        $scope.inProcess = false;

        $scope.errorDetected = false;
        $scope.errorMessage = '';

        $scope.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.translate = function() {
            $scope.inProcess = true;
            I18NService.translateViaExternalApi($scope.config).then(function(response) {
                $uibModalInstance.close(response);
            }, function(error) {
                $scope.errorDetected = true;
                $scope.errorMessage = error.data.message;
            }).
            finally(function() {
                $scope.inProcess = false;
            })
        }
    });