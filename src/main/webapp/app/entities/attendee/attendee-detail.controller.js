(function() {
    'use strict';

    angular
        .module('attendanceApp')
        .controller('AttendeeDetailController', AttendeeDetailController);

    AttendeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Attendee'];

    function AttendeeDetailController($scope, $rootScope, $stateParams, previousState, entity, Attendee) {
        var vm = this;

        vm.attendee = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('attendanceApp:attendeeUpdate', function(event, result) {
            vm.attendee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
