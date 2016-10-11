(function() {
    'use strict';

    angular
        .module('attendanceApp')
        .controller('AttendeeController', AttendeeController);

    AttendeeController.$inject = ['$scope', '$state', 'Attendee'];

    function AttendeeController ($scope, $state, Attendee) {
        var vm = this;
        
        vm.attendees = [];

        loadAll();

        function loadAll() {
            Attendee.query(function(result) {
                vm.attendees = result;
            });
        }
    }
})();
