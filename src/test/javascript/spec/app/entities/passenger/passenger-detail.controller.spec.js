'use strict';

describe('Controller Tests', function() {

    describe('Passenger Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPassenger, MockFlightSlice;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPassenger = jasmine.createSpy('MockPassenger');
            MockFlightSlice = jasmine.createSpy('MockFlightSlice');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Passenger': MockPassenger,
                'FlightSlice': MockFlightSlice
            };
            createController = function() {
                $injector.get('$controller')("PassengerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'flightalertApp:passengerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
