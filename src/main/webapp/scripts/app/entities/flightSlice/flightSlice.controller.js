'use strict';

angular.module('flightalertApp')
    .controller('FlightSliceController', function ($scope, $state, FlightSlice, FlightSliceSearch, ParseLinks) {

        $scope.flightSlices = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            FlightSlice.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.flightSlices.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.flightSlices = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            FlightSliceSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.flightSlices = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.flightSlice = {
                origin: null,
                destination: null,
                preferredCabin: null,
                date: null,
                maxStops: null,
                maxConnectionDurationInMinutes: null,
                maxPriceInCHF: null,
                refundable: null,
                id: null
            };
        };
    });
