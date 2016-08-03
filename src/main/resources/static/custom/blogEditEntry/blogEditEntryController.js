/**
 * Created by DruidKuma on 7/21/16.
 */
angular.module("blogApp")
    .controller('BlogEditEntryController',['$scope', 'BlogEntry', '$routeParams', '$sce', '$location', 'Country', 'Upload', function($scope, BlogEntry, $routeParams, $sce, $location, Country, Upload) {

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

        $scope.loadBlogPost = function() {
            if($routeParams.id) {
                $scope.loadingProcess = true;
                BlogEntry.single($routeParams.id).then(function(response) {
                    $scope.postEntry = response.data;
                    $scope.pageHeading.title = "Blog Entry Editor";
                    //$scope.editEntryModel = $scope.postEntry.content;

                }).finally(function() {
                    $scope.loadingProcess = false;
                });
            }
        };

        $scope.chosenCountries = [
            { name: "America", isoCode: "US" },
            { name: "Spain", isoCode: "ES" },
            { name: "Germany", isoCode: "DE" },
            { name: "Ukraine", isoCode: "UA" }
        ];


        $scope.loadCountries = function($query) {
            return Country.flags().then(function(response) {
                var countries = response.data;
                return countries.filter(function(country) {
                    return country.name.toLowerCase().indexOf($query.toLowerCase()) != -1;
                });
            });
        };

        $scope.openImageGallery = function() {
            Custombox.open({
                target: "#imageGalleryModal",
                effect: "push",
                overlaySpeed: 100,
                overlayColor: "#36404a",
                overlayOpacity: 1
            });

            $scope.galleryImages = [
                'dist/images/gallery/1.jpg',
                'dist/images/gallery/2.jpg',
                'dist/images/gallery/3.jpg',
                'dist/images/gallery/4.jpg',
                'dist/images/gallery/5.jpg',
                'dist/images/gallery/6.jpg',
                'dist/images/gallery/7.jpg',
                'dist/images/gallery/8.jpg',
                'dist/images/gallery/9.jpg',
                'dist/images/gallery/10.jpg',
                'dist/images/gallery/11.jpg',
                'dist/images/gallery/12.jpg'
            ];
            $scope.selectedImage = '';
        };

        $scope.chooseImage = function(image) {
            $scope.selectedImage = image;
        };

        $scope.addChosenImage = function() {
            $scope.postEntry.content += '<img src="'+$scope.selectedImage+'"/>';
            $scope.closeImageGallery();
        };

        $scope.closeImageGallery = function() {
            delete $scope.selectedImage;
            delete $scope.galleryImages;
            Custombox.close();
        };

        $scope.loadGalleryPart = function() {
            $scope.loadingGalleryProcess = true;
        };

        $scope.uploadImage = function(file, errFiles) {
            if (file) {
                Upload.upload({
                    url: BASE_URL + '/api/blog/image/upload',
                    data: {file: file}
                }).then(function (response) {
                    $scope.loadGalleryPart();
                }, function (errResponse) {
                    if (errResponse.status > 0)
                        $scope.errorMsg = errResponse.status + ': ' + errResponse.data;
                });
            }
        };

        $scope.loadBlogPost();
    }]);