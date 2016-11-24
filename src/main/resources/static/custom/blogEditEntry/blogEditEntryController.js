/**
 * Created by DruidKuma on 7/21/16.
 */
angular.module("blogApp")
    .controller('BlogEditEntryController',['$scope', 'BlogEntry', '$routeParams', '$sce', '$location', 'Country', 'Category', 'Upload', 'Media', '$translatePartialLoader', '$translate', function($scope, BlogEntry, $routeParams, $sce, $location, Country, Category, Upload, Media, $translatePartialLoader, $translate) {

        //Page Heading
        $scope.$on('$routeChangeSuccess', function () {
            $scope.pageHeading.title = "Blog Entry Editor";
            $translatePartialLoader.addPart('components.category');
            $translate.refresh();
        });

        $scope.loadingGalleryProcess = false;

        $scope.tinyMceOptions = {
            image_advtab: true,
            image_title: true
        };

        $scope.$on('countryChanged', function() {
            $location.path("/list");
        });

        $scope.galleryPaging = {
            currentPage: 1,
            totalItems: 1000,
            itemsPerPage: 10,
            maxSize: 5
        };

        $scope.deleteCaption = function() {
            delete $scope.postEntry.captionSrc;
        };

        $scope.loadBlogPost = function() {
            if($routeParams.id && $routeParams.id != 'new') {
                $scope.loadingProcess = true;
                BlogEntry.single($routeParams.id).then(function(response) {
                    $scope.postEntry = response.data;
                }).finally(function() {
                    $scope.loadingProcess = false;
                });
            }
            else {
                $scope.postEntry = {
                    countries: [$scope.currentCountry]
                };
            }
        };

        $scope.loadCountries = function($query) {
            return Country.flags().then(function(response) {
                var countries = response.data;
                return countries.filter(function(country) {
                    return country.name.toLowerCase().indexOf($query.toLowerCase()) != -1;
                });
            });
        };

        $scope.loadCategories = function($query) {
            return Category.forEntryEdit().then(function(response) {
                var categories = response.data;
                return categories.filter(function(category) {
                    return $scope.isCategoryValidForChoice(category, $query) && category.nameKey.toLowerCase().indexOf($query.toLowerCase()) != -1;
                });
            });
        };

        $scope.isCategoryValidForChoice = function(category) {
            var isValid = true;
            angular.forEach($scope.postEntry.countries, function(country) {
                if (!category.countries.filter(function(e) { return e.isoCode == country.isoCode; }).length > 0) {
                    isValid = false;
                }
            });
            return isValid;
        };

        $scope.$watchCollection('postEntry.countries', function(newValue, oldValue) {
            if(newValue) {
                $scope.postEntry.categories = $scope.postEntry.categories.filter($scope.isCategoryValidForChoice);
            }
        });

        $scope.closeEditPanel = function() {
            $location.path("/entry/" + $routeParams.id);
        };

        $scope.openImageGallery = function(isCaption) {
            Custombox.open({
                target: "#imageGalleryModal",
                effect: "push",
                overlaySpeed: 100,
                overlayColor: "#36404a",
                overlayOpacity: 1
            });

            $scope.galleryImages = [];
            $scope.selectedImage = {};
            $scope.isForCaption = isCaption;
            $scope.loadGalleryPart();
        };

        $scope.openSeoSettings = function() {
            Custombox.open({
                target: "#seoSettingsModal",
                effect: "push",
                overlaySpeed: 100,
                overlayColor: "#36404a",
                overlayOpacity: 1
            });
        };
        $scope.closeSeoSettings = function() {
            Custombox.close();
        };

        $scope.chooseImage = function(image) {
            $scope.selectedImage = image;
        };

        $scope.addChosenImage = function() {
            if($scope.isForCaption) {
                $scope.postEntry.captionSrc = $scope.selectedImage.fullImgSrc;
            }
            else {
                $scope.postEntry.content += '<img src="'+$scope.selectedImage.fullImgSrc+'"/>';
            }
            $scope.closeImageGallery();
        };

        $scope.closeImageGallery = function() {
            delete $scope.selectedImage;
            delete $scope.galleryImages;
            delete $scope.isForCaption;
            Custombox.close();
        };

        $scope.loadGalleryPart = function() {
            $scope.loadingGalleryProcess = true;
            Media.page({page: $scope.galleryPaging.currentPage-1, numOnPage: $scope.galleryPaging.itemsPerPage}).then(function(response) {
                $scope.galleryImages = response.data.content;
                $scope.galleryPaging.totalItems = response.data.totalElements;
            }).finally(function() {
                $scope.loadingGalleryProcess = false;
            });
        };

        $scope.uploadImage = function(file, errFiles) {
            if (file) {
                $scope.loadingGalleryProcess = true;
                Upload.upload({
                    url: BASE_URL + '/api/blog/image/upload',
                    data: {file: file}
                }).then(function (response) {
                    $scope.loadGalleryPart();
                }, function (errResponse) {
                    if (errResponse.status > 0)
                        $scope.errorMsg = errResponse.status + ': ' + errResponse.data;
                }).finally(function() {
                    $scope.loadGalleryPart();
                });
            }
        };

        $scope.generatePermalink = function() {
            BlogEntry.generatePermalink($scope.postEntry.title, $scope.postEntry.id, $scope.postEntry.creationDate).then(function(response) {
                $scope.postEntry.permalink = response.data.permalink;
            });
        };

        $scope.saveBlogEntry = function() {
            if($scope.validateBlogEntryForm()) {
                $scope.loadingProcess = true;
                BlogEntry.savePost($scope.postEntry).then(function(response) {
                    $location.path('/entry/' + response.data.id);
                }, function(error, status) {
                    if(error.data.status == 409) {
                        $scope.postEntryFormError.permalink = {
                            error: true,
                            message: 'Blog Entry with such permalink already exists'
                        };
                    }
                }).finally(function() {
                    $scope.loadingProcess = false;
                });
            }
        };

        $scope.postEntryFormError = {};
        $scope.validateBlogEntryForm = function() {
            var isValid = true;
            if(!$scope.postEntry.permalink) {
                $scope.postEntryFormError.permalink = {error: true};
                isValid = false;
            }
            if(!$scope.postEntry.title) {
                $scope.postEntryFormError.title = {error: true};
                isValid = false;
            }
            if(!$scope.postEntry.captionSrc) {
                $scope.postEntryFormError.caption = {error: true};
                isValid = false;
            }
            if(!$scope.postEntry.content) {
                $scope.postEntryFormError.content = {error: true};
                isValid = false;
            }
            if(!$scope.postEntry.countries || $scope.postEntry.countries.length < 1) {
                $scope.postEntryFormError.countries = {error: true};
                isValid = false;
            }
            if(!$scope.postEntry.categories || $scope.postEntry.categories.length < 1) {
                $scope.postEntryFormError.categories = {error: true};
                isValid = false;
            }

            return isValid;
        };

        $scope.removeAttribute = function(attribute) {
            delete $scope.postEntryFormError[attribute];
        };

        $scope.loadBlogPost();
    }]);