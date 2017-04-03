'use strict';

angular.module('flightalertApp')
    .controller('FlightRequestDetailController', function ($scope, $rootScope, $stateParams, entity, FlightRequest, FlightSlice) {
        $scope.flightRequest = entity;
        $scope.load = function (id) {
            FlightRequest.get({id: id}, function(result) {
                $scope.flightRequest = result;
            });
        };
        var unsubscribe = $rootScope.$on('flightalertApp:flightRequestUpdate', function(event, result) {
            $scope.flightRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
