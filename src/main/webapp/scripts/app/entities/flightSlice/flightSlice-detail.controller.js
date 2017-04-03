'use strict';

angular.module('flightalertApp')
    .controller('FlightSliceDetailController', function ($scope, $rootScope, $stateParams, entity, FlightSlice, PermittedCarrier, Passenger, FlightRequest) {
        $scope.flightSlice = entity;
        $scope.load = function (id) {
            FlightSlice.get({id: id}, function(result) {
                $scope.flightSlice = result;
            });
        };
        var unsubscribe = $rootScope.$on('flightalertApp:flightSliceUpdate', function(event, result) {
            $scope.flightSlice = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
