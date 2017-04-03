'use strict';

angular.module('flightalertApp')
    .controller('PassengerController', function ($scope, $state, Passenger, PassengerSearch) {

        $scope.passengers = [];
        $scope.loadAll = function() {
            Passenger.query(function(result) {
               $scope.passengers = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            PassengerSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.passengers = result;
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
            $scope.passenger = {
                passengerType: null,
                id: null
            };
        };
    });
