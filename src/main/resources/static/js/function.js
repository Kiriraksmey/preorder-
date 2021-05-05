function setInputFilter(textbox, inputFilter) {
    ["input", "keydown", "keyup", "mousedown", "mouseup", "select", "contextmenu", "drop"].forEach(function (event) {
        textbox.addEventListener(event, function () {
            if (inputFilter(this.value)) {
                this.oldValue = this.value;
                this.oldSelectionStart = this.selectionStart;
                this.oldSelectionEnd = this.selectionEnd;
            } else if (this.hasOwnProperty("oldValue")) {
                this.value = this.oldValue;
                this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
            }
        });
    });
}

function checkFormCyberCard() {
    var cardNumber = $('#cardNumber').val();
    var cardHolderName = $('#cardHolderName').val();
    var expirationDate = $('#expirationDate').val();
    var ccvNumber = $('#ccvNumber').val();
    var paymentType = $('#paymentType').val();
    if (!cardNumber || 0 === cardNumber.length) {
        $('#cardNumber').focus();
        $('#span_error_card_number').html(CONFIG_LANG['error.card.number']);
        return false;
    }

    //nghiem thu xong se bo comment
    /*
    switch (paymentType) {
        case 'visa':
            if(cardNumber.length > 19) {
                $('#span_error_card_number').html(CONFIG_LANG['error.card.number.visa.length']);
                return false;
            }
            if(cardNumber[0] !== '4') {
                $('#span_error_card_number').html(CONFIG_LANG['error.card.number.visa.fist']);
                return false;
            }
            break;
        case 'jcb':
            if(cardNumber.length < 16 || cardNumber.length > 19) {
                $('#span_error_card_number').html(CONFIG_LANG['error.card.number.jcb.length']);
                return false;
            }
            var fourDigits = parseInt(cardNumber.substring(0,4));
            if(fourDigits < 3528 || fourDigits > 3589) {
                $('#span_error_card_number').html(CONFIG_LANG['error.card.number.jcb.first']);
                return false;
            }
            break;
        case 'mastercard':
            if(cardNumber.length !== 16){
                $('#span_error_card_number').html(CONFIG_LANG['error.card.number.master.length']);
                return false;
            }
            if(cardNumber[0] !== '2' && cardNumber[0] !== '5') {
                $('#span_error_card_number').html(CONFIG_LANG['error.card.number.master.first.digit']);
                return false;
            }
            if(cardNumber[0] === '2'){
                if(parseInt(cardNumber[1]) < 2 || parseInt(cardNumber[1]) > 7) {
                    $('#span_error_card_number').html(CONFIG_LANG['error.card.number.master.second.digit.with2']);
                    return false;
                }
                var sixDigits = parseInt(cardNumber.substring(0,6));
                if(sixDigits < 222100 || sixDigits > 272099){
                    $('#span_error_card_number').html(CONFIG_LANG['error.card.number.master.first.six.digits.with2']);
                    return false;
                }
            }
            if(cardNumber[0] === '5'){
                if(parseInt(cardNumber[1]) < 1 || parseInt(cardNumber[1]) > 5) {
                    $('#span_error_card_number').html(CONFIG_LANG['error.card.number.master.second.digit.with5']);
                    return false;
                }
                var sixDigits = parseInt(cardNumber.substring(0,6));
                if(sixDigits < 510000 || sixDigits > 559999){
                    $('#span_error_card_number').html(CONFIG_LANG['error.card.number.master.first.six.digits.with5']);
                    return false;
                }
            }
            break;
    }
     */
    //end

    if (!cardHolderName || 0 === cardHolderName.length) {
        $('#cardHolderName').focus();
        $('#span_error_card_name').html(CONFIG_LANG['error.card.name']);
        return false;
    }
    if (!expirationDate || 0 === expirationDate.length) {
        $('#expirationDate').focus();
        $('#span_error_expiration_date').html(CONFIG_LANG['error.card.expired.empty']);
        return false;
    }

    var dateString = document.getElementById("expirationDate").value;
    var regex = /((0[1-9]|1[0-2])\/(\d{2}))$/;
    if (!regex.test(dateString)) {
        $('#span_error_expiration_date').html(CONFIG_LANG['error.card.expired']);
        return false;
    }

    if (!ccvNumber || 0 === ccvNumber.length) {
        $('#expirationDate').focus();
        $('#span_error_cvv_number').html(CONFIG_LANG['error.card.cvv']);
        return false;
    }
    if (ccvNumber.length !== 3) {
        $('#ccvNumber').focus();
        $('#span_error_cvv_number').html(CONFIG_LANG['error.card.cvv.number.invalid']);
        return false;
    }
    document.getElementById('overlay').style.visibility = "visible";
}

function submitInitalPayment() {
    // document.getElementById('overlay').style.visibility = "visible";
    $('#span_error_payment_type').html('');
}

function showOverlayIndex() {
    document.getElementById('overlay').style.visibility = "visible";
}

function hideOverlayIndex() {
    document.getElementById('overlay').style.visibility = "hidden";
}


function showOverlaySubmit() {

    if ($.cookie("paymentAmount")) {
        $('#txt_paymentamount').val($.cookie("paymentAmount"));
    }
    var topupAmout = $('#txt_topupamount').val();

    var isdn = $('#txt_phonenumber').val();
    var captcha = $('#captcha').val();
    var paymentAmount = $('#txt_paymentamount').val();
    hideAllValidateError();
    if (!isdn || 0 === isdn.length) {
        $('#txt_phonenumber').focus();
        window.scrollTo(0, 0);
        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.empty']);
        return false;
    }
    if (checkNotInvalidIsdnMetfone()) {
        $('#txt_phonenumber').focus();
        window.scrollTo(0, 0);
        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.invalid']);
        $('#span_error_txt_phone').show();
        return false;
    }

    if (!topupAmout || 0 === topupAmout.length) {
        $('#span_error_txt_topamount').html(CONFIG_LANG['error.topup.amount.empty']);
        $('#span_error_txt_topamount').show();
        $('#txt_topupamount').focus();
        window.scrollTo(0, 0);
        return false;
    }

    if (parseInt(topupAmout) > 100) {
        $('#span_error_txt_topamount').html(CONFIG_LANG['error.payment.limit']);
        $('#txt_topupamount').focus();
        return false;
    }

    if (!atLeastOnePaymentType()) {
        $('#span_error_payment_type').html(CONFIG_LANG['error.payment.type']);
        window.scrollTo(0, 350);
        hideKeyboadMobile();
        // window.scrollTo(0, 800);
        // $("html, body").animate({ scrollTop: 900 }, 1000);
        return false;
    }

    if (!captcha || 0 === captcha.length) {
        hideKeyboadMobile();
        $('#captcha').focus();
        $('#span_error_captcha').html(CONFIG_LANG['error.captcha.empty']);
        window.scrollTo(0, 1400);
        return false;
    }


    var paymentMethod = $('input[name=paymentMethod]:checked').val();
    var paymentMethodUpper = paymentMethod.toUpperCase();
    //Xử lý tiếp chỗ này cho emoney
    if (paymentMethodUpper === CONFIG.emoney.toUpperCase() && checkAccountEmoneyEmpty()) {
        $('#account_emoney').focus();
        window.scrollTo(0, 800);
        $('#span_error_account_emoney').html(CONFIG_LANG['error.account.emoney.invalid']);
        return false;
    }

    if ($('#check_agree:checked').val()) {
        // document.getElementById('overlay').style.visibility = "visible";
    } else {
        $('#span_error_checkagree').html(CONFIG_LANG['error.term.invalid']);
        return false;
    }

    if (paymentMethodUpper === CONFIG.abapay.toUpperCase() || paymentMethodUpper === CONFIG.master.toUpperCase() ||
        paymentMethodUpper === CONFIG.visa.toUpperCase() || paymentMethodUpper === CONFIG.jcb.toUpperCase()) {

        var data = {
            "phoneNumber": isdn,
            "topupAmount": topupAmout,
            "paymentAmount": paymentAmount,
            "paymentMethod": paymentMethod,
            "captcha": captcha
        };

        var request = $.ajax({
            url: CONFIG.url + '/createTransAba',
            contentType: "application/json",
            type: "POST",
            data: JSON.stringify(data),
            dataType: "json"
        }).done(function (result) {
            if (result.status === true) {
                if (paymentMethodUpper === CONFIG.abapay.toUpperCase()) {
                    $('#aba_merchant_request').attr('action', result.urlABA);
                    $('#aba_hash').val(result.hash);
                    $('#aba_tran_id').val(result.txnid);
                    $('#aba_amount').val(result.amt_string);
                    $('#aba_firstname').val(result.firstname);
                    $('#aba_lastname').val(result.lastname);
                    $('#aba_phone').val(result.phone);
                    $('#aba_email').val(result.email);

                    AbaPayway.checkout();

                    var countTime = CONFIG.total_time_aba_pay;
                    var tranIdABA = $.cookie("transIdAba");

                    var data = {
                        "isdn": "",
                        "nttrefid": tranIdABA,
                        "txnid": tranIdABA
                    };

                    if (!tranIdABA) {
                        $.ajax({
                            url: CONFIG.url + "/checkQRScan",
                            contentType: "application/json",
                            type: "POST",
                            dataType: "json",
                            data: JSON.stringify(data),
                            success:
                                function (d) {
                                },
                            error: function (e) {
                                alert(CONFIG_LANG['error.isdn.system']);
                            }
                        });
                        redirectHomepage();
                        return;
                    }

                    setTimeout(function () {
                        sendRequest();
                    }, 2000);
                    var countDownInterval = setInterval(function () {
                        countTime--;
                        countDownFunc();
                    }, 1000);


                    function sendRequest() {
                        var data = {
                            "isdn": "",
                            "nttrefid": tranIdABA,
                            "txnid": tranIdABA
                        };

                        var ifConnected = window.navigator.onLine;
                        if (ifConnected) {
                            $.ajax({
                                url: CONFIG.url + "/checkQRScan",
                                contentType: "application/json",
                                type: "POST",
                                dataType: "json",
                                data: JSON.stringify(data),
                                success:
                                    function (data) {
                                        if (data.respcode !== '02') {
                                            var dataForm = {
                                                'login': '',
                                                'respcode': data.respcode,
                                                'respdesc': data.respdesc,
                                                'txncurr': data.txncurr,
                                                'amt': data.amt,
                                                'txnid': data.txnid,
                                                'proc_code': data.proc_code,
                                                'nttrefid': data.nttrefid,
                                                'signature': data.signature,
                                                'udf1': data.udf1,
                                                'udf2': data.udf2,
                                                'udf3': data.udf3,
                                                'udf4': data.udf4,
                                                'udf5': data.udf5,
                                                'date': data.date
                                            };
                                            var form = document.createElement('form');
                                            document.body.appendChild(form);
                                            form.method = 'post';
                                            form.action = CONFIG.url + '/webhook';
                                            for (var name in dataForm) {
                                                var input = document.createElement('input');
                                                input.type = 'hidden';
                                                input.name = name;
                                                input.value = dataForm[name];
                                                form.appendChild(input);
                                            }
                                            clearInterval(countDownInterval);
                                            form.submit();
                                            return;
                                        } else {
                                            if (countTime < 0) {
                                                var dataForm = {
                                                    'login': '',
                                                    'respcode': '01',
                                                    'respdesc': 'error.payment.timeout',
                                                    'txncurr': data.txncurr,
                                                    'amt': data.amt,
                                                    'txnid': data.txnid,
                                                    'proc_code': data.proc_code,
                                                    'nttrefid': data.nttrefid,
                                                    'signature': data.signature,
                                                    'udf1': data.udf1,
                                                    'udf2': data.udf2,
                                                    'udf3': data.udf3,
                                                    'udf4': data.udf4,
                                                    'udf5': data.udf5,
                                                    'date': data.date
                                                };
                                                var form = document.createElement('form');
                                                document.body.appendChild(form);
                                                form.method = 'post';
                                                form.action = CONFIG.url + '/webhook';
                                                for (var name in dataForm) {
                                                    var input = document.createElement('input');
                                                    input.type = 'hidden';
                                                    input.name = name;
                                                    input.value = dataForm[name];
                                                    form.appendChild(input);
                                                }
                                                clearInterval(countDownInterval);
                                                form.submit();
                                                return;
                                            }
                                        }

                                    },
                                error: function (e) {
                                    alert(CONFIG_LANG['error.isdn.system']);
                                    clearInterval(countDownInterval);
                                    return;
                                    var dataForm = {
                                        'login': '',
                                        'respcode': '01',
                                        'respdesc': 'error.isdn.system',
                                        'txncurr': data.txncurr,
                                        'amt': data.amt,
                                        'txnid': data.txnid,
                                        'proc_code': data.proc_code,
                                        'nttrefid': data.nttrefid,
                                        'signature': data.signature,
                                        'udf1': data.udf1,
                                        'udf2': data.udf2,
                                        'udf3': data.udf3,
                                        'udf4': data.udf4,
                                        'udf5': data.udf5,
                                        'date': data.date
                                    };
                                    var form = document.createElement('form');
                                    document.body.appendChild(form);
                                    form.method = 'post';
                                    form.action = CONFIG.url + '/webhook';
                                    for (var name in dataForm) {
                                        var input = document.createElement('input');
                                        input.type = 'hidden';
                                        input.name = name;
                                        input.value = dataForm[name];
                                        form.appendChild(input);
                                    }
                                    clearInterval(countDownInterval);
                                    form.submit();
                                    return;
                                }
                            });
                        } else {
                            alert(CONFIG_LANG['error.isdn.system']);
                            clearInterval(countDownInterval);
                            return;
                        }

                    };

                    function countDownFunc() {
                        if (countTime % CONFIG.time_wait_aba === 0) {
                            sendRequest();
                        }
                        if (countTime < 0) {
                            sendRequest();
                            clearInterval(countDownInterval);
                        } else {
                            $('#invoice_timing').text(countTime);
                        }
                    }

                } else {
                    //submit form

                    var form = document.createElement('form');
                    document.body.appendChild(form);
                    form.method = 'post';
                    form.action = result.urlABA;

                    var input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = 'hash';
                    input.value = result.hash;
                    form.appendChild(input);

                    input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = 'tran_id';
                    input.value = result.txnid;
                    form.appendChild(input);

                    input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = 'amount';
                    input.value = result.amt_string;
                    form.appendChild(input);

                    input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = 'firstname';
                    input.value = result.firstname;
                    form.appendChild(input);

                    input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = 'lastname';
                    input.value = result.lastname;
                    form.appendChild(input);

                    input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = 'phone';
                    input.value = result.phone;
                    form.appendChild(input);

                    input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = 'email';
                    input.value = result.email;
                    form.appendChild(input);

                    input = document.createElement('input');
                    input.type = 'hidden';
                    input.name = 'payment_option';
                    input.value = 'cards';
                    form.appendChild(input);
                    console.log(result);

                    form.submit();
                }

            } else {
                renewCaptcha();
                switch (result.error_field) {
                    case CONFIG.isdn_error_field:
                        $('#txt_phonenumber').focus();
                        window.scrollTo(0, 0);
                        $('#span_error_txt_phone').html(result.error_description);
                        $('#span_error_txt_phone').show();
                        return false;
                        break;
                    case CONFIG.amount_error_field:
                        $('#span_error_txt_topamount').html(result.error_description);
                        $('#span_error_txt_topamount').show();
                        $('#txt_topupamount').focus();
                        window.scrollTo(0, 0);
                        break;
                    case CONFIG.payment_error_field:
                        $('#span_error_payment_type').html(result.error_description);
                        $('#span_error_payment_type').show();
                        window.scrollTo(0, 350);
                        hideKeyboadMobile();
                        break;
                    case CONFIG.captcha_error_field:
                        $('#captcha').val('');
                        $('#captcha').focus();
                        window.scrollTo(0, 1400);
                        $('#span_error_captcha').html(result.error_description);
                        $('#span_error_captcha').show();
                        break;
                }
            }

            return false;
        }).fail(function (error) {
            $("#txt_paymentamount").val('');
            $("#txt_topupamount").val('');
            $("#txt_hidden_paymentAmount").val('');
            $("#div_deposit_phone").removeClass("success_deposit").removeClass("require_deposit");
            return false;
        });
        return false;
    }

    if (paymentMethodUpper === CONFIG.acleda.toUpperCase()) {

        var data = {
            "phoneNumber": isdn,
            "topupAmount": topupAmout,
            "paymentAmount": paymentAmount,
            "paymentMethod": paymentMethod,
            "captcha": captcha
        };

        var request = $.ajax({
            url: CONFIG.url + '/createTransAcleda',
            contentType: "application/json",
            type: "POST",
            data: JSON.stringify(data),
            dataType: "json"
        }).done(function (result) {
            if (result.status === true) {
                //submit form

                var form = document.createElement('form');
                document.body.appendChild(form);
                form.method = 'post';
                form.action = result.urlAcleda;

                var input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'merchantID';
                input.value = result.merchantID;
                form.appendChild(input);

                input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'sessionid';
                input.value = result.sessionid;
                form.appendChild(input);

                input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'paymenttokenid';
                input.value = result.paymenttokenid;
                form.appendChild(input);

                input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'description';
                input.value = result.description;
                form.appendChild(input);

                input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'expirytime';
                input.value = result.expirytime;
                form.appendChild(input);

                input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'amount';
                input.value = result.amt_string;
                form.appendChild(input);

                input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'quantity';
                input.value = result.quantity;
                form.appendChild(input);

                input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'item';
                input.value = result.item;
                form.appendChild(input);

                input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'invoiceid';
                input.value = result.invoiceid;
                form.appendChild(input);

                input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'currencytype';
                input.value = result.currencytype;
                form.appendChild(input);

                input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'transactionID';
                input.value = result.transactionID;
                form.appendChild(input);

                input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'successUrlToReturn';
                input.value = result.successUrlToReturn;
                form.appendChild(input);

                input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'errorUrl';
                input.value = result.errorUrl;
                form.appendChild(input);

                form.submit();

            } else {
                renewCaptcha();
                switch (result.error_field) {
                    case CONFIG.isdn_error_field:
                        $('#txt_phonenumber').focus();
                        window.scrollTo(0, 0);
                        $('#span_error_txt_phone').html(result.error_description);
                        $('#span_error_txt_phone').show();
                        return false;
                        break;
                    case CONFIG.amount_error_field:
                        $('#span_error_txt_topamount').html(result.error_description);
                        $('#span_error_txt_topamount').show();
                        $('#txt_topupamount').focus();
                        window.scrollTo(0, 0);
                        break;
                    case CONFIG.payment_error_field:
                        $('#span_error_payment_type').html(result.error_description);
                        $('#span_error_payment_type').show();
                        window.scrollTo(0, 350);
                        hideKeyboadMobile();
                        break;
                    case CONFIG.captcha_error_field:
                        $('#captcha').val('');
                        $('#captcha').focus();
                        window.scrollTo(0, 1400);
                        $('#span_error_captcha').html(result.error_description);
                        $('#span_error_payment_type').show();
                        break;
                }
            }

            return false;
        }).fail(function (error) {
            renewCaptcha();
            $("#txt_paymentamount").val('');
            $("#txt_topupamount").val('');
            $("#txt_hidden_paymentAmount").val('');
            $("#div_deposit_phone").removeClass("success_deposit").removeClass("require_deposit");
            return false;
        });
        return false;
    }
}

function confirmStep1() {
    var isdn = $('#txt_phonenumber').val();
    var topupAmout = $('#txt_topupamount').val();
    if (!isdn || 0 === isdn.length) {
        $('#txt_phonenumber').focus();
        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.empty']);
        $('#span_error_txt_phone').show();
        return false;
    }
    if (!topupAmout || 0 === topupAmout.length) {
        $('#txt_topupamount').focus();
        return false;
    }


    var data = {"isdn": isdn, "amoutTopup": topupAmout};
    hideAllValidateError();
    showOverlayIndex();
    var request = $.ajax({
        url: CONFIG.url + '/checkisdn',
        contentType: "application/json",
        type: "POST",
        data: JSON.stringify(data),
        dataType: "json"
    }).done(function (result) {
        if (result.responseCode === '00') {
            $("#div_deposit_phone").removeClass("success_deposit").removeClass("require_deposit").addClass("success_deposit");
            $("#txt_topupamount").val(result.amoutTopup);
            $("#txt_paymentamount").val(result.paymentTopup);
            $("#txt_hidden_paymentAmount").val(result.paymentTopup);
            return true;
        } else {
            $("#div_deposit_phone").removeClass("success_deposit").removeClass("require_deposit");
            $("#txt_paymentamount").val('');
            $("#txt_topupamount").val('');
            $("#txt_hidden_paymentAmount").val('');
            uncheckAllRadio();
            switch (result.responseCode) {
                case '01':
                    if (result.responseMessage === 'error.isdn.invalid') {
                        $('#txt_phonenumber').focus();
                        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.invalid']);
                        $('#span_error_txt_phone').show();
                    }
                    if (result.responseMessage === 'error.isdn.empty') {
                        $('#txt_phonenumber').focus();
                        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.empty']);
                        $('#span_error_txt_phone').show();
                    }
                    break;
                case '99':
                    $('#txt_phonenumber').focus();
                    $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.system']);
                    $('#span_error_txt_phone').show();
                    break;
            }
            return false;
        }
    }).fail(function (error) {
        $("#txt_paymentamount").val('');
        $("#txt_topupamount").val('');
        $("#txt_hidden_paymentAmount").val('');
        $("#div_deposit_phone").removeClass("success_deposit").removeClass("require_deposit");
        return false;
    });
}

function openPopupTerm() {
    $('#popup-term').addClass("open");
}

function openPopupError() {
    $('#popup-error').addClass("open");
}

function closePopupTerm() {
    $('#popup-term').removeClass("open");
}

function openPopupInvoice() {
    $('#popup-invoice').addClass("open");
}

function closePopupInvoice() {
    $('#popup-invoice').removeClass("open");
}


function getListPayment() {
    // hideAllValidateError();
    showOverlayIndex();
    var request = $.ajax({
        url: CONFIG.url + '/getListPayment',
        contentType: "application/json",
        type: "POST",
        dataType: "json"
    }).done(function (result) {
        hideOverlayIndex();
        if (result.responseCode === '00') {
            var lstPayment = result.lst;
            for (var i = 0; i < lstPayment.length; i++) {
                var item = lstPayment[i];
                if (item.paramValue === CONFIG.visa) {
                    if (item.status === 1) {
                        $('#li_visa').removeClass();
                    } else {
                        $('#li_visa').removeClass().addClass('disable');
                    }
                }
                if (item.paramValue === CONFIG.abapay) {
                    if (item.status === 1) {
                        $('#li_abapay').removeClass();
                    } else {
                        $('#li_abapay').removeClass().addClass('disable');
                    }
                }
                if (item.paramValue === CONFIG.master) {
                    if (item.status === 1) {
                        $('#li_mastercard').removeClass();
                    } else {
                        $('#li_mastercard').removeClass().addClass('disable');
                    }
                }
                if (item.paramValue === CONFIG.jcb) {
                    if (item.status === 1) {
                        $('#li_jcb').removeClass();
                    } else {
                        $('#li_jcb').removeClass().addClass('disable');
                    }
                }
                if (item.paramValue === CONFIG.alipay) {
                    if (item.status === 1) {
                        $('#li_alipay').removeClass();
                    } else {
                        $('#li_alipay').removeClass().addClass('disable');
                    }
                }

                if (item.paramValue === CONFIG.wechat) {
                    if (item.status === 1) {
                        $('#li_wechat').removeClass();
                    } else {
                        $('#li_wechat').removeClass().addClass('disable');
                    }
                }
                if (item.paramValue === CONFIG.union) {
                    if (item.status === 1) {
                        $('#li_union').removeClass();
                    } else {
                        $('#li_union').removeClass().addClass('disable');
                    }
                }

                if (item.paramValue === CONFIG.emoney) {
                    if (item.status === 1) {
                        $('#li_emoney').removeClass();
                    } else {
                        $('#li_emoney').removeClass().addClass('disable');
                    }
                }
                if (item.paramValue === CONFIG.acleda) {
                    if (item.status === 1) {
                        $('#li_acleda_bank').removeClass();
                    } else {
                        $('#li_acleda_bank').removeClass().addClass('disable');
                    }
                }
                if (item.paramValue === CONFIG.wing) {
                    if (item.status === 1) {
                        $('#li_wing').removeClass();
                    } else {
                        $('#li_wing').removeClass().addClass('disable');
                    }
                }
            }
        } else {
            $('#li_visa').removeClass().addClass('disable');
            $('#li_mastercard').removeClass().addClass('disable');
            $('#li_jcb').removeClass().addClass('disable');
            $('#li_alipay').removeClass().addClass('disable');
            $('#li_wechat').removeClass().addClass('disable');
            $('#li_union').removeClass().addClass('disable');
            $('#li_emoney').removeClass().addClass('disable');
            $('#li_abapay').removeClass().addClass('disable');
            $('#li_acleda_bank').removeClass().addClass('disable');
            $('#li_wing').removeClass().addClass('disable');
        }
    }).fail(function (error) {
        $('#li_visa').removeClass().addClass('disable');
        $('#li_mastercard').removeClass().addClass('disable');
        $('#li_jcb').removeClass().addClass('disable');
        $('#li_alipay').removeClass().addClass('disable');
        $('#li_wechat').removeClass().addClass('disable');
        $('#li_union').removeClass().addClass('disable');
        $('#li_emoney').removeClass().addClass('disable');
        $('#li_abapay').removeClass().addClass('disable');
        $('#li_acleda_bank').removeClass().addClass('disable');
        $('#li_wing').removeClass().addClass('disable');
    });
}

function checkNotInvalidIsdnMetfone() {
    var value = $('#txt_phonenumber').val();
    return (!/^071/.test(value) && !/^097/.test(value) && !/^088/.test(value) && !/^068/.test(value)
        && !/^090/.test(value) && !/^067/.test(value) && !/^060/.test(value)
        && !/^031/.test(value) && !/^066/.test(value)
        && !/^71/.test(value) && !/^97/.test(value) && !/^88/.test(value) && !/^68/.test(value)
        && !/^90/.test(value) && !/^67/.test(value) && !/^60/.test(value)
        && !/^31/.test(value) && !/^66/.test(value));
}

function callCheckIsdnServicePayment(isdn, amount, paymentMethod) {
    if (checkNotInvalidIsdnMetfone()) {
        $('#txt_phonenumber').focus();
        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.invalid']);
        $('#span_error_txt_phone').show();
        return;
    }
    var accountEmoney = $('#account_emoney').val();
    if (!accountEmoney) {
        accountEmoney = "";
    }
    var data = {"isdn": isdn, "amoutTopup": amount, "paymentType": paymentMethod, "accountEmoney": accountEmoney};
    hideAllValidateError();
    showOverlayIndex();
    switch (amount) {
        case '2':
        case 2:
            uncheckAllRadio();
            document.getElementById('radio2').checked = true;
            break;
        case '5':
        case 5:
            uncheckAllRadio();
            document.getElementById('radio5').checked = true;
            break;
        case '10':
        case 10:
            uncheckAllRadio();
            document.getElementById('radio10').checked = true;
            break;
        case '20':
        case 20:
            uncheckAllRadio();
            document.getElementById('radio20').checked = true;
            break;
        case '50':
        case 50:
            uncheckAllRadio();
            document.getElementById('radio50').checked = true;
            break;
        case '100':
        case 100:
            uncheckAllRadio();
            document.getElementById('radio100').checked = true;
            break;
        default:
            uncheckAllRadio();
            break;
    }

    $("#captcha").focus();

    var request = $.ajax({
        url: CONFIG.url + '/checkisdn',
        contentType: "application/json",
        type: "POST",
        data: JSON.stringify(data),
        dataType: "json"
    }).done(function (result) {
        hideOverlayIndex();
        if (result.responseCode === '00') {
            if (checkNotInvalidIsdnMetfone()) {
                $("#div_deposit_phone").removeClass("success_deposit").removeClass("require_deposit");
            } else {
                $("#div_deposit_phone").removeClass("success_deposit").removeClass("require_deposit").addClass("success_deposit");
            }
            $("#txt_topupamount").val(result.amoutTopup);
            $("#txt_paymentamount").val(result.paymentTopup);
            $("#txt_hidden_paymentAmount").val(result.paymentTopup);
        } else {
            $("#div_deposit_phone").removeClass("success_deposit").removeClass("require_deposit");
            $("#txt_paymentamount").val('');
            $("#txt_topupamount").val('');
            $("#txt_hidden_paymentAmount").val('');
            uncheckAllRadio();
            switch (result.responseCode) {
                case '01':
                    if (result.responseMessage === 'error.isdn.invalid') {
                        $('#txt_phonenumber').focus();
                        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.invalid']);
                        $('#span_error_txt_phone').show();
                    }
                    if (result.responseMessage === 'error.isdn.empty') {
                        $('#txt_phonenumber').focus();
                        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.empty']);
                        $('#span_error_txt_phone').show();
                    }
                    if (result.responseMessage === 'error.payment.invalid') {
                        $('#txt_topupamount').focus();
                        $('#span_error_txt_topamount').html(CONFIG_LANG['error.amount.invalid']);
                        $('#span_error_txt_topamount').show();
                    }
                    break;
                case '99':
                    $('#txt_phonenumber').focus();
                    $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.system']);
                    $('#span_error_txt_phone').show();
                    break;
            }
        }
    }).fail(function (error) {
        $("#txt_paymentamount").val('');
        $("#txt_topupamount").val('');
        $("#txt_hidden_paymentAmount").val('');
        $("#div_deposit_phone").removeClass("success_deposit").removeClass("require_deposit");
        hideOverlayIndex();
    });
}

function callCheckIsdnService(isdn, amount, paymentMethod) {
    if (checkNotInvalidIsdnMetfone()) {
        $('#txt_phonenumber').focus();
        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.invalid']);
        $('#span_error_txt_phone').show();
        return;
    }
    var accountEmoney = $('#account_emoney').val();
    if (!accountEmoney) {
        accountEmoney = "";
    }
    var data = {"isdn": isdn, "amoutTopup": amount, "paymentType": paymentMethod, "accountEmoney": accountEmoney};
    hideAllValidateError();
    showOverlayIndex();
    switch (amount) {
        case '2':
        case 2:
            uncheckAllRadio();
            document.getElementById('radio2').checked = true;
            break;
        case '5':
        case 5:
            uncheckAllRadio();
            document.getElementById('radio5').checked = true;
            break;
        case '10':
        case 10:
            uncheckAllRadio();
            document.getElementById('radio10').checked = true;
            break;
        case '20':
        case 20:
            uncheckAllRadio();
            document.getElementById('radio20').checked = true;
            break;
        case '50':
        case 50:
            uncheckAllRadio();
            document.getElementById('radio50').checked = true;
            break;
        case '100':
        case 100:
            uncheckAllRadio();
            document.getElementById('radio100').checked = true;
            break;
        default:
            uncheckAllRadio();
            break;
    }

    var request = $.ajax({
        url: CONFIG.url + '/checkisdn',
        contentType: "application/json",
        type: "POST",
        data: JSON.stringify(data),
        dataType: "json"
    }).done(function (result) {
        hideOverlayIndex();
        if (result.responseCode === '00') {
            if (checkNotInvalidIsdnMetfone()) {
                $("#div_deposit_phone").removeClass("success_deposit").removeClass("require_deposit");
            } else {
                $("#div_deposit_phone").removeClass("success_deposit").removeClass("require_deposit").addClass("success_deposit");
            }
            $("#txt_topupamount").val(result.amoutTopup);
            $("#txt_paymentamount").val(result.paymentTopup);
            $("#txt_hidden_paymentAmount").val(result.paymentTopup);
        } else {
            $("#div_deposit_phone").removeClass("success_deposit").removeClass("require_deposit");
            $("#txt_paymentamount").val('');
            // $("#txt_topupamount").val('');
            $("#txt_hidden_paymentAmount").val('');
            // uncheckAllRadio();
            switch (result.responseCode) {
                case '01':
                    if (result.responseMessage === 'error.isdn.invalid') {
                        $('#txt_phonenumber').focus();
                        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.invalid']);
                        $('#span_error_txt_phone').show();
                    }
                    if (result.responseMessage === 'error.isdn.empty') {
                        $('#txt_phonenumber').focus();
                        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.empty']);
                        $('#span_error_txt_phone').show();
                    }
                    if (result.responseMessage === 'error.payment.invalid') {
                        if (paymentMethod === CONFIG.emoney) {
                            $('#account_emoney').focus();
                            window.scrollTo(0, 800);
                            $('#span_error_account_emoney').html(CONFIG_LANG['error.account.emoney']);
                        } else {
                            $('#txt_topupamount').focus();
                            $('#span_error_txt_topamount').html(CONFIG_LANG['error.amount.invalid']);
                            $('#span_error_txt_topamount').show();
                        }

                    }
                    break;
                case '99':
                    $('#txt_phonenumber').focus();
                    $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.system']);
                    $('#span_error_txt_phone').show();
                    break;
            }
        }
    }).fail(function (error) {
        $("#txt_paymentamount").val('');
        $("#txt_topupamount").val('');
        $("#txt_hidden_paymentAmount").val('');
        $("#div_deposit_phone").removeClass("success_deposit").removeClass("require_deposit");
        hideOverlayIndex();
    });
}

function uncheckAllRadio() {
    document.getElementById('radio2').checked = false;
    document.getElementById('radio5').checked = false;
    document.getElementById('radio10').checked = false;
    document.getElementById('radio20').checked = false;
    document.getElementById('radio50').checked = false;
    document.getElementById('radio100').checked = false;
}

function checkIsdnEmpty() {
    var isdn = $('#txt_phonenumber').val();
    if (!isdn || 0 === isdn.length) {
        return true;
    }
    return false;
}

function checkAccountEmoneyEmpty() {
    var accountEmoney = $('#account_emoney').val();
    if (!accountEmoney || 0 === accountEmoney.length) {
        return true;
    }
    return false;
}

function onKeypressTopupAmount() {
    hideAllValidateErrorAmount();
    // if (checkIsdnEmpty()) {
    //     uncheckAllRadio();
    //     $('#txt_phonenumber').focus();
    //     $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.empty']);
    //     $('#span_error_txt_phone').show();
    //     return;
    // }
}

function onFocusoutPhoneNumber() {
    var topupAmout = $('#txt_topupamount').val();
    if (checkIsdnEmpty()) {
        return;
    }
    if (checkNotInvalidIsdnMetfone()) {
        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.invalid']);
        $('#span_error_txt_phone').show();
    } else {
        $('#span_error_txt_phone').html('');
        $('#span_error_txt_phone').hide();
    }
    if (!topupAmout || 0 === topupAmout.length) {
        $('#txt_topupamount').focus();
        $('#span_error_txt_topamount').html(CONFIG_LANG['error.amount.invalid']);
        return;
    }
    checkISDN();
    // if (checkIsdnEmpty()) {
    //     uncheckAllRadio();
    //     $('#txt_phonenumber').focus();
    //     $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.empty']);
    //     $('#span_error_txt_phone').show();
    //     return;
    // }
}

function onFocusoutAccountMoney() {
    if (checkAccountEmoneyEmpty()) {
        return;
    }
    hideAllValidateError();
    checkISDNFocusOutEmoney();
}

function onKeypressPhoneNumber() {
    uncheckAllRadio();
    hideAllValidateError();
}

function onKeypressAccountMoney() {
    hideAllValidateError();
}

function hideKeyboadMobile() {
    $(document.activeElement).filter(':input:focus').blur();
}

function selectAmout(amount) {
    uncheckAllRadio();
    var isdn = $('#txt_phonenumber').val();
    $("#txt_topupamount").val(amount);
    document.getElementById('radio' + amount).checked = true;
    hideAllValidateError();
    if (checkIsdnEmpty()) {
        $('#txt_phonenumber').focus();
        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.empty']);
        $('#span_error_txt_phone').show();
        return;
    }
    if (!atLeastOnePaymentType()) {
        $('#span_error_payment_type').html(CONFIG_LANG['error.payment.type']);
        hideKeyboadMobile();
        // window.scrollTo(0, 800);
        // $("html, body").animate({ scrollTop: 900 }, 1000);
        return;
    }
    var paymentMethod = $('input[name=paymentMethod]:checked').val();

    if (paymentMethod === CONFIG.emoney) {
        var eMoneyAcc = $('#account_emoney').val();
        if (!eMoneyAcc) {
            $('#account_emoney').focus();
            window.scrollTo(0, 800);
            $('#span_error_account_emoney').html(CONFIG_LANG['error.account.emoney.invalid']);
            return false;
        }
    }

    callCheckIsdnService(isdn, amount, paymentMethod);
}

function atLeastOnePaymentType() {
    return ($('input[name=paymentMethod]:checked').length > 0);
}

function agreeTerm() {
    $("#check_agree").prop("checked", true);
    $('#popup-term').removeClass("open");
}

function checkISDNPayment() {
    var isdn = $('#txt_phonenumber').val();
    var topupAmout = $('#txt_topupamount').val();

    if (!isdn || 0 === isdn.length) {
        $('#txt_phonenumber').focus();
        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.empty']);
        $('#span_error_txt_phone').show();
        return;
    }

    if (!topupAmout || 0 === topupAmout.length) {

        $('#span_error_txt_topamount').html(CONFIG_LANG['error.amount.invalid']);
        $('#txt_topupamount').focus();
        return;
    } else {
        $('#span_error_txt_topamount').html('');
    }

    if (!topupAmout || parseInt(topupAmout) > 100) {
        $('#span_error_txt_topamount').html(CONFIG_LANG['error.payment.limit']);
        $('#txt_topupamount').focus();
        return;
    }

    if (!atLeastOnePaymentType()) {
        $('#span_error_payment_type').html(CONFIG_LANG['error.payment.type']);
        hideKeyboadMobile();
        // $("html, body").animate({ scrollTop: 900 }, 1000);
        return;
    } else {
        $('#span_error_payment_type').html('');
    }

    var paymentMethod = $('input[name=paymentMethod]:checked').val();

    //Xử lý tiếp chỗ này cho emoney
    if ((paymentMethod && paymentMethod === CONFIG.emoney) || (paymentMethod && paymentMethod === CONFIG.wing)) {
        $('#div_AccountEmoney').show();
    } else {
        $('#div_AccountEmoney').hide();
    }

    if (paymentMethod === CONFIG.emoney) {
        $('#account_emoney').val(isdn.substring(1, isdn.length));
        document.getElementById("labelEmoney").style.display = "grid";
        document.getElementById("labelWing").style.display = "none";
        $("#account_emoney").attr("placeholder", CONFIG_LANG['enter_account_emoney']);
    }
    if (paymentMethod === CONFIG.wing) {
        $('#account_emoney').val(isdn.substring(1, isdn.length));
        document.getElementById("labelEmoney").style.display = "none";
        document.getElementById("labelWing").style.display = "grid";
        $("#account_emoney").attr("placeholder", CONFIG_LANG['enter_account_wing']);
    }

    callCheckIsdnServicePayment(isdn, topupAmout, paymentMethod);
}

function checkISDNFocusOutEmoney() {
    var isdn = $('#txt_phonenumber').val();
    var topupAmout = $('#txt_topupamount').val();

    var paymentMethod = $('input[name=paymentMethod]:checked').val();
    //Xử lý tiếp chỗ này cho emoney
    if (paymentMethod && paymentMethod === CONFIG.emoney) {
        $('#div_AccountEmoney').show();
    } else {
        $('#div_AccountEmoney').hide();
    }

    if (!topupAmout || 0 === topupAmout.length) {
        console.log(topupAmout);
        $('#span_error_txt_topamount').html(CONFIG_LANG['error.amount.invalid']);
        $('#txt_topupamount').focus();
        window.scrollTo(0, 0);
        return;
    } else {
        $('#span_error_txt_topamount').html('');
    }

    if (!atLeastOnePaymentType()) {
        $('#span_error_payment_type').html(CONFIG_LANG['error.payment.type']);
        hideKeyboadMobile();
        window.scrollTo(0, 350);
        // $("html, body").animate({ scrollTop: 900 }, 1000);
        return;
    } else {
        $('#span_error_payment_type').html('');
    }

    if (!topupAmout || parseInt(topupAmout) > 100) {
        $('#span_error_txt_topamount').html(CONFIG_LANG['error.payment.limit']);
        $('#txt_topupamount').focus();
        window.scrollTo(0, 0);
        return;
    }
    if (!isdn || 0 === isdn.length) {
        $('#txt_phonenumber').focus();
        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.empty']);
        $('#span_error_txt_phone').show();
        window.scrollTo(0, 0);
        return;
    }
    checkISDN();
}

function checkISDN() {
    var isdn = $('#txt_phonenumber').val();
    var topupAmout = $('#txt_topupamount').val();

    var paymentMethod = $('input[name=paymentMethod]:checked').val();
    //Xử lý tiếp chỗ này cho emoney
    if (paymentMethod && paymentMethod === CONFIG.emoney) {
        $('#div_AccountEmoney').show();
    } else {
        $('#div_AccountEmoney').hide();
    }

    if (!isdn || 0 === isdn.length) {
        $('#txt_phonenumber').focus();
        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.empty']);
        $('#span_error_txt_phone').show();
        return;
    }

    if (!topupAmout || 0 === topupAmout.length) {
        $('#span_error_txt_topamount').html(CONFIG_LANG['error.amount.invalid']);
        $('#txt_topupamount').focus();
        return;
    } else {
        $('#span_error_txt_topamount').html('');
    }

    if (!topupAmout || parseInt(topupAmout) > 100) {
        $('#span_error_txt_topamount').html(CONFIG_LANG['error.payment.limit']);
        $('#txt_topupamount').focus();
        return;
    }

    if (!atLeastOnePaymentType()) {
        $('#span_error_payment_type').html(CONFIG_LANG['error.payment.type']);
        hideKeyboadMobile();
        // window.scrollTo(0, 200);
        // $("html, body").animate({ scrollTop: 900 }, 1000);
        return;
    } else {
        $('#span_error_payment_type').html('');
    }

    callCheckIsdnService(isdn, topupAmout, paymentMethod);
}

function focusToIsdn() {
    $('#txt_phonenumber').focus();
}

function hideAllValidateErrorAmount() {
    $("#span_error_txt_phone").html('');
    $("#span_error_txt_topamount").html('');
    $("#span_error_txt_paymentamount").html('');
    $("#span_error_captcha").html('');
    $("#span_error_checkagree").html('');
    $('#span_error_payment_type').html('');
    $('#span_error_account_emoney').html('');
}

function hideAllValidateError() {
    $("#span_error_txt_phone").html('');
    $("#span_error_txt_topamount").html('');
    $("#span_error_txt_paymentamount").html('');
    $("#span_error_captcha").html('');
    $("#span_error_checkagree").html('');
    $('#span_error_payment_type').html('');
    $('#span_error_account_emoney').html('');

    var value = $('#txt_phonenumber').val();
    if (checkNotInvalidIsdnMetfone()) {
        $('#txt_phonenumber').focus();
        $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.invalid']);
        $('#span_error_txt_phone').show();
    }
}

function onChangeRadioMoney() {
    $("input[name='radio_amount']").change(function () {
        onKeypressTopupAmount();
    });
}

function onChangeRadioPaymentMethod() {
    $("input[name='radioPaymentType']").change(function () {
        onKeypressTopupAmount();
    });
}

function pageShow(event) {
    console.log('pageshow');
    var historyTraversal = event.persisted ||
        (typeof window.performance != "undefined" &&
            window.performance.navigation.type === 2);
    if (historyTraversal) {
        // Handle page restore.
        window.location.reload(true);
    }
}

function onloadIndex() {

    setInputFilter(document.getElementById("txt_phonenumber"), function (value) {
        if (!/^\d*$/.test(value)) {
            // uncheckAllRadio();
            $('#txt_phonenumber').focus();
            $('#span_error_txt_phone').html(CONFIG_LANG['error.phone.number.invalid']);
            $('#span_error_txt_phone').show();
        }
        if (checkNotInvalidIsdnMetfone()) {
            $('#txt_phonenumber').focus();
            $('#span_error_txt_phone').html(CONFIG_LANG['error.isdn.invalid']);
            $('#span_error_txt_phone').show();
        }
        return /^\d*$/.test(value);
        // return /^071\d$/.test(value);
    });
    setInputFilter(document.getElementById("txt_topupamount"), function (value) {
        if ((!/^\d*$/.test(value) && parseInt(value) > 0) || (value === "")) {
            uncheckAllRadio();
            // $('#span_error_txt_topamount').html(CONFIG_LANG['error.topup.number.greater']);
        }
        return (/^\d*$/.test(value) && (parseInt(value) > 0)) || (value === "");
    });
    setInputFilter(document.getElementById("account_emoney"), function (value) {
        if ((!/^\d*$/.test(value) && parseInt(value) > 0) || (value === "")) {
            var paymentMethod = $('input[name=paymentMethod]:checked').val();
            $('#account_emoney').focus();
            if (paymentMethod === CONFIG.emoney) {
                $('#span_error_account_emoney').html(CONFIG_LANG['error.account.emoney.notnull']);
            }
            if (paymentMethod === CONFIG.wing) {
                $('#span_error_account_emoney').html(CONFIG_LANG['error.account.wing.notnull']);
            }
            $('#span_error_account_emoney').show();
            window.scrollTo(0, 1300);
        }
        return (/^\d*$/.test(value) && (parseInt(value) > 0)) || (value === "");
    });
    if (atLeastOnePaymentType()) {
        var paymentMethod = $('input[name=paymentMethod]:checked').val();
        //Xử lý tiếp chỗ này cho emoney
        if (paymentMethod && paymentMethod === CONFIG.emoney) {
            $('#account_emoney').focus();
            $('#div_AccountEmoney').show();
            window.scrollTo(0, 800);
        }
    }
    // focusToIsdn();
    onChangeRadioMoney();
}

function onloadPaymentCyber() {
    setInputFilter(document.getElementById("cardNumber"), function (value) {
        $('#span_error_card_number').html('');
        if (!/^\d*$/.test(value)) {
            $('#span_error_card_number').html(CONFIG_LANG['error.card.number.invalid']);
            if (checkIsdnEmpty()) {
                $('#txt_phonenumber').focus();
            }
        }
        // return /^\d*$/.test(value);
    });

    setInputFilter(document.getElementById("ccvNumber"), function (value) {
        if (!/^\d*$/.test(value)) {
            $('#span_error_card_number').html(CONFIG_LANG['error.card.cvv.number.invalid']);
        } else {
            $('#span_error_card_number').html('');
        }
        return /^\d*$/.test(value);
    });
}

function validateExpiration(event) {

    var inputChar = String.fromCharCode(event.keyCode);
    var code = event.keyCode;
    var allowedKeys = [8];
    if (allowedKeys.indexOf(code) !== -1) {
        return;
    }

    event.target.value = event.target.value.replace(
        /^([1-9]\/|[2-9])$/g, '0$1/' // 3 > 03/
    ).replace(
        /^(0[1-9]|1[0-2])$/g, '$1/' // 11 > 11/
    ).replace(
        /^([0-1])([3-9])$/g, '0$1/$2' // 13 > 01/3
    ).replace(
        /^(0?[1-9]|1[0-2])([0-9]{2})$/g, '$1/$2' // 141 > 01/41
    ).replace(
        /^([0]+)\/|[0]+$/g, '0' // 0/ > 0 and 00 > 0
    ).replace(
        /[^\d\/]|^[\/]*$/g, '' // To allow only digits and `/`
    ).replace(
        /\/\//g, '/' // Prevent entering more than 1 `/`
    );

    var lblError = document.getElementById("span_error_expiration_date");
    lblError.innerHTML = "";
    var dateString = document.getElementById("expirationDate").value;
    var regex = /((0[1-9]|1[0-2])\/(\d{2}))$/;
    if (regex.test(dateString)) {
        lblError.innerHTML = "";
        return true;
    } else {
        lblError.innerHTML = CONFIG_LANG['error.card.expired'];
        return false;
    }
}

function isDateGreaterThanToday(b) {
    var dS = b.split("/");
    var d1 = new Date(dS[1], (+dS[0] - 1));
    var today = new Date();
    if (d1 > today) {
        return true;
    } else {
        return false;
    }
}

function validateCVV() {
    var lblError = document.getElementById("span_error_cvv_number");
    lblError.innerHTML = "";
    var ccvNumber = document.getElementById("ccvNumber").value;
    var regex = /\d{3}$/;
    if (regex.test(ccvNumber)) {
        lblError.innerHTML = "";
        return true;
    } else {
        lblError.innerHTML = CONFIG_LANG['error.card.cvv.number.invalid']
        return false;
    }
}

function toUpperName() {
    $('#span_error_card_name').html('');
    var x = document.getElementById("cardHolderName");
    var name = x.value;
    if (/^(\w+\s?)*\s*\s*$/.test(name)) {
        name = name.replace(/\s+/g, ' ');
    } else {
        name = name.substring(0, name.length - 1);
    }
    x.value = name.toUpperCase();
}

function redirectHomepage() {
    window.location.href = CONFIG.url;
}

function redirectTopup() {
    window.location.href = CONFIG.url + '/topup';
}

function validateCaptcha(event) {
    $('#span_error_captcha').html('');
}

function renewCaptcha() {
    $.ajax({
        url: CONFIG.url + '/captcha',
        type: "GET",
        headers: {
            "X-TOKEN": 'xxxxx'
        }
    }).done(function (data) {
        // $('#captchaImg').attr('src', "");
        // setTimeout(function(){
        // $('#captchaImg').attr('src', CONFIG.url + '/captcha');   // set the image source
        $('img[id=captchaImg]').prop('src', CONFIG.url + '/captcha?' + new Date().getTime());
        // $('#captchaImg').attr('src','data:image/jpeg;base64,' + base64Encode(data));
        // }, 0);
        // document.getElementById('captchaImg').src = CONFIG.url + '/captcha';

    }).fail(function () {
    });
}

function onKeyUpTopupAmount() {
    var topupAmout = $('#txt_topupamount').val();
    if (!topupAmout || 0 === topupAmout.length) {
        $('#span_error_txt_topamount').html(CONFIG_LANG['error.amount.invalid']);
        $('#txt_topupamount').focus();
        return;
    } else {
        $('#span_error_txt_topamount').html('');
    }
}