'use strict';

describe('Controller Tests', function() {

    describe('FlightSlice Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFlightSlice, MockPermittedCarrier, MockPassenger, MockFlightRequest;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFlightSlice = jasmine.createSpy('MockFlightSlice');
            MockPermittedCarrier = jasmine.createSpy('MockPermittedCarrier');
            MockPassenger = jasmine.createSpy('MockPassenger');
            MockFlightRequest = jasmine.createSpy('MockFlightRequest');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FlightSlice': MockFlightSlice,
                'PermittedCarrier': MockPermittedCarrier,
                'Passenger': MockPassenger,
                'FlightRequest': MockFlightRequest
            };
            createController = function() {
                $injector.get('$controller')("FlightSliceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'flightalertApp:flightSliceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
