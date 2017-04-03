'use strict';

angular.module('flightalertApp')
    .controller('PermittedCarrierDetailController', function ($scope, $rootScope, $stateParams, entity, PermittedCarrier, FlightSlice) {
        $scope.permittedCarrier = entity;
        $scope.load = function (id) {
            PermittedCarrier.get({id: id}, function(result) {
                $scope.permittedCarrier = result;
            });
        };
        var unsubscribe = $rootScope.$on('flightalertApp:permittedCarrierUpdate', function(event, result) {
            $scope.permittedCarrier = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
