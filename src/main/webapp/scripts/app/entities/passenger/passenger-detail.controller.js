'use strict';

angular.module('flightalertApp')
    .controller('PassengerDetailController', function ($scope, $rootScope, $stateParams, entity, Passenger, FlightSlice) {
        $scope.passenger = entity;
        $scope.load = function (id) {
            Passenger.get({id: id}, function(result) {
                $scope.passenger = result;
            });
        };
        var unsubscribe = $rootScope.$on('flightalertApp:passengerUpdate', function(event, result) {
            $scope.passenger = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
