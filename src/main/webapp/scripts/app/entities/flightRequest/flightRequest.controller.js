'use strict';

angular.module('flightalertApp')
    .controller('FlightRequestController', function ($scope, $state, FlightRequest, FlightRequestSearch, ParseLinks) {

        $scope.flightRequests = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            FlightRequest.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.flightRequests.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.flightRequests = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            FlightRequestSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.flightRequests = result;
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
            $scope.flightRequest = {
                validFromDate: null,
                validToDate: null,
                id: null
            };
        };
    });
