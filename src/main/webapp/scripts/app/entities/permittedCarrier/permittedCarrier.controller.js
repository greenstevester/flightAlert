'use strict';

angular.module('flightalertApp')
    .controller('PermittedCarrierController', function ($scope, $state, PermittedCarrier, PermittedCarrierSearch) {

        $scope.permittedCarriers = [];
        $scope.loadAll = function() {
            PermittedCarrier.query(function(result) {
               $scope.permittedCarriers = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            PermittedCarrierSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.permittedCarriers = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.permittedCarrier = {
                carrierCode: null,
                id: null
            };
        };
    });
