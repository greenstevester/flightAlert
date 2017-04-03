'use strict';

describe('Controller Tests', function() {

    describe('PermittedCarrier Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPermittedCarrier, MockFlightSlice;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPermittedCarrier = jasmine.createSpy('MockPermittedCarrier');
            MockFlightSlice = jasmine.createSpy('MockFlightSlice');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PermittedCarrier': MockPermittedCarrier,
                'FlightSlice': MockFlightSlice
            };
            createController = function() {
                $injector.get('$controller')("PermittedCarrierDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'flightalertApp:permittedCarrierUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
