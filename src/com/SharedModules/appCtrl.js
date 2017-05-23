angular.module('app.controllers.app', [])
    .controller('AppCtrl',
        function ($scope, $rootScope, $ionicPlatform, $state,
            $ionicSideMenuDelegate, $ionicHistory, $q, stateService,
            feedbackService, customer, promotions, welcomeCentreFactory,
            appConfig, welcomeBillingFactory) {

            $scope.logged_in = stateService.isLoggedIn();
            $scope.welcomeCentreCustomer = stateService.isWelcomeCentreCustomer();

            $scope.leaveFeedback = feedbackService.leaveFeedback;
            $scope.canWeLeaveFeedback = false;
            $scope.showBills = false;

            updateState();

            $rootScope.$on('loggedInStateChanged', function (event, state) {
                $scope.logged_in = state;
                $scope.welcomeCentreCustomer = stateService.isWelcomeCentreCustomer();

                updateState();
            });

            $scope.do_logout = function () {
                stateService.logout();
                $ionicSideMenuDelegate.toggleLeft(false);
                $ionicHistory.nextViewOptions({
                    disableAnimate: true,
                    disableBack: true
                });
                $scope.showBills = false;
            };

            $ionicPlatform.ready(function () {
                if (!feedbackService.isCordovaPresent()) return;

                feedbackService.isAvailable().then(function () {
                    $scope.canWeLeaveFeedback = true;
                });
            });

            function updateState() {
                if (stateService.isLoggedIn()) {
                    updateMobileCust();
                    updatePromotions();
                    updateShowBills();
                }
            }

            function updateShowBills() {
                if (!stateService.isWelcomeCentreCustomer()) {
                    $scope.showBills = true;
                    return;
                }

                var account = customer.getPrimaryAccount();

                var billing = account.then(function (account) {
                    if (account.state.preTrio && !account.orderId) {
                        return $q.reject(new Error(
                            "No orderId for preTrio customer"));
                    }

                    return welcomeCentreFactory.getOrder(
                        account.state.preTrio ? account.orderId :
                        account.phoneNumbers[0].number
                    );
                }).then(function (order) {

                    return welcomeCentreFactory.getBillingInformation(
                        order.modules.billing_information.url,
                        appConfig.type
                    );
                });

                $q.all({
                    account: account,
                    billing: billing
                }).then(function (data) {


                    return welcomeBillingFactory.getEstimatedBillData(
                        data.billing,
                        data.account.packageContents
                    );
                }).then(function (data) {
                    $scope.showBills = data.realBillAvailable;
                }).catch(function (error) {
                    console.log(error);
                });
            }

            function updateMobileCust() {

                if (!$scope.logged_in) {
                    $scope.isMobileCust = null;
                    return;
                }

                customer.getPrimaryAccount().then(function (account) {
                    if (account && account.packageContents) {
                        $scope.isMobileCust = account.packageContents.mobile;
                    }
                });
            }

            function updatePromotions() {
                // This is for diplaying promotions in the menu
                customer.getPrimaryAccount().then(function (account) {
                    return promotions.activePromotions(account.id);
                }).then(function (promos) {
                    $scope.promotions = promos;
                }, function (error) {
                    console.error(error);
                });
            }
        });
