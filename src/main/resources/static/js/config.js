var CONFIG = {
    //'url': 'http://localhost:8080',
    // 'url' : 'http://192.168.0.126:8080',
    //'url' : 'https://pay.metfone.com.kh/metfone-topup',
    // 'url' : 'https://pay.metfone.com.kh',
    // 'url' : 'http://123.31.38.148:8856/metfone-topup',
    // 'url' : 'http://metfone.kabarplus.online/metfone-topup/',
    'url' : 'https://pay.metfone.com.kh',
    'time_wait_aba': 15,
    'total_time_aba': 90,
    'total_time_acleda': 90,
    'total_time_aba_pay': 120,
    'time_wait': 10000,
    'total_time_wait': 600000,
    'visa' : 'Visa',
    'alipay': 'Alipay',
    'wechat': 'Wechat',
    'emoney': 'eMoney',
    'master': 'Master',
    'jcb' : 'JCB',
    'union': 'Union',
    'abapay': 'ABAPay',
    'acleda': 'Acleda',
    'isdn_error_field': 'ISDN',
    'amount_error_field': 'TOPUP_AMOUNT',
    'payment_error_field': 'PAYMENT_METHOD',
    'captcha_error_field': 'CAPTCHA',

};

var CONFIG_LG_TYPE = {
    'en': '??locale.language_en??',
    'kh': '??locale.language_kh??',
    'cn': '??locale.language_cn??'
};

var CONFIG_EN = {
    'error.captcha.empty' : 'Captcha is empty',
    'error.isdn.empty': ' Phone number is empty',
    'error.isdn.invalid': 'Phone number is invalid',
    'error.isdn.system': 'System is busy. Please try again later',
    'error.amount.invalid': 'Topup amount is invalid',
    'error.payment_amount.invalid': 'Payment amount is invalid',
    'error.card_data.invalid': 'Card data is invalid',
    'error.payment.type': 'Please select at least one payment method',
    'error.card.number': 'Please input card number',
    'error.card.name': 'Please input card holder name',
    'error.card.cvv': 'Please input CVV number',
    'error.card.expired': 'Enter date in MM/YY format ONLY',
    'error.card.expired.empty': 'Please input expiration date',
    'error.captcha.invalid': 'Please verify captcha before submit',
    'error.term.invalid': 'Please check to agreed term and condition',
    'error.phone.number.invalid': 'Phone number must be number',
    'error.card.number.invalid': 'Card number must be number',
    'error.topup.amount.number.invalid': 'Topup amount must be number',
    'error.card.cvv.number.invalid': 'Enter CVV in three number ONLY.',
    'error.topup.number.greater' : 'Topup amount must be number and greater than 0',
    'error.date.greater.current' : 'Enter date is greater than current date',
    'error.payment.timeout' : 'Payment process timeout',
    'error.payment.limit': 'Maximum topup amount is $100',
    'error.topup.amount.empty': 'Topup amount is empty',
    'error.card.number.visa.length': 'Visa card number length must be 19',
    'error.card.number.visa.fist': 'Fist digit of visa card number must be 4',
    'error.card.number.jcb.length': 'JCB card number length must be from 16 to 19',
    'error.card.number.jcb.first' : 'First 4 digits of JCB must be in the range 3528 through 3589',
    'error.card.number.master.length': 'Mastercard card number length must be 16',
    'error.card.number.master.first.digit': 'First digit of Mastercard must be 2 or 5',
    'error.card.number.master.second.digit.with2' : 'Second digit of this Mastercard must be in the range 2 through 7',
    'error.card.number.master.first.six.digits.with2' : 'First 6 digits of this Mastercard must be in the range 222100 through 272099',
    'error.card.number.master.second.digit.with5' : 'Second digit of this Mastercard must be in the range 1 through 5',
    'error.card.number.master.first.six.digits.with5' : 'First 6 digits of this Mastercard must be in the range 510000 through 559999',
    'error.account.emoney.invalid': 'eMoney account must be number',
    'error.account.emoney': 'eMoney account is invalid',

};

var CONFIG_KH = {
    'error.captcha.empty' : 'Captcha គឺទទេ',
    'error.isdn.empty': 'លេខទូរស័ព្ទមិនមានក្នុងបណ្តាញ',
    'error.isdn.invalid': 'លេខទូរស័ព្ទមិនត្រឹមត្រូវ',
    'error.isdn.system': 'ប្រព័ន្ធកំពុងជាប់រវល់ សូម​ព្យាយាម​ម្តង​ទៀត​នៅ​ពេល​ក្រោយ',
    'error.amount.invalid': 'ចំនួនទឹកប្រាក់បញ្ចូលមិនត្រឹមត្រូវ',
    'error.payment_amount.invalid': 'ចំនួនសាច់ប្រាក់ដែលទូរទាត់មិនត្រឹមត្រូវ',
    'error.card_data.invalid': 'ទិន្នន័យកាតមិនត្រឹមត្រូវ',
    'error.payment.type': 'សូមជ្រើសរើសវិធីសាស្ត្រក្នុងការបង់ទឹកប្រាក់',
    'error.card.number': 'សូមបំពេញទិន្នន័យកាតម្តងទៀត',
    'error.card.name': 'សូមបំពេញទិន្នន័យអ្នក​កាន់​កាតម្តងទៀតe',
    'error.card.cvv': 'សូមបំពេញទិន្នន័យលេខ​​ CVV ម្តងទៀត',
    'error.card.expired': 'ទម្រង់សំរាប់បំពេញកាលបរិច្ឆេទ ខែ/ឆ្នាំ',
    'error.card.expired.empty': 'សូមបំពេញកាលបរិច្ឆេទ​ផុតកំណត់',
    'error.captcha.invalid': 'សូមផ្ទៀងផ្ទាត់ captcha មុនពេលបញ្ជូន',
    'error.term.invalid': 'សូមត្រួតពិនិត្យល័ក្ខខ័ណ្ឌដែលបានព្រមព្រៀង',
    'error.phone.number.invalid': 'លេខទូរស័ព្ទដែលត្រូវបំពេញ',
    'error.card.number.invalid': 'លេខកាតដែលត្រូវបំពេញ',
    'error.topup.amount.number.invalid': 'ចំនួនទឹកប្រាក់ដែលត្រូវបញ្ជូល',
    'error.card.cvv.number.invalid': 'CVV​ បញ្ជូលបានតែបីលេខប៉ុណ្ណោះ',
    'error.topup.number.greater' : 'ការបញ្ចូលទឹកប្រាក់ត្រូវតែជាតួលេខហើយមានចំនួនធំជាងលេខ ០ ',
    'error.date.greater.current' : 'Enter date is greater than current date',
    'error.payment.timeout' : 'ដំណើរការទូទាត់បានផុតកំណត់',
    'error.payment.limit': 'ចំនួនទឹកប្រាក់បញ្ចូលអតិបរមាគឺ ១០០ ដុល្លារ',
    'error.topup.amount.empty': 'ចំនួនទឹកប្រាក់បញ្ចូលទឹកប្រាក់គឺទទេ។',
    'error.card.number.visa.length': 'ប្រវែងលេខកាតវីសាត្រូវតែមាន ១៩ ។',
    'error.card.number.visa.fist': 'ខ្ទង់ដំបូងនៃលេខកាតវីសាត្រូវតែ ៤ ។',
    'error.card.number.jcb.length': 'ប្រវែងលេខកាត JCB ត្រូវមានចាប់ពី ១៦ ដល់ ១៩ ។',
    'error.card.number.jcb.first' : 'លេខ JCB ៤ ខ្ទង់ដំបូងត្រូវតែស្ថិតនៅចន្លោះ ៣៥២៨ ដល់ ៣៥៨៩ ។',
    'error.card.number.master.length': 'ប្រវែងលេខកាតម៉ាស្ទ័រត្រូវតែមាន ១៦ ។',
    'error.card.number.master.first.digit': 'ខ្ទង់ទីមួយនៃម៉ាស្ទ័រកាតត្រូវតែមានលេខ ២ រឺ ៥ ។',
    'error.card.number.master.second.digit.with2' : 'ខ្ទង់ទី ២ នៃម៉ាស្ទ័រកាតនេះត្រូវតែស្ថិតនៅចន្លោះទី ២ ដល់ ៧ ។',
    'error.card.number.master.first.six.digits.with2' : '៦ ខ្ទង់ដំបូងនៃម៉ាស្ទ័រកាតនេះត្រូវតែស្ថិតនៅចន្លោះ ២២២២០០ ដល់ ២៧២០៩៩ ។',
    'error.card.number.master.second.digit.with5' : 'ខ្ទង់ទី ២ នៃម៉ាស្ទ័រកាតនេះត្រូវតែស្ថិតនៅចន្លោះ ១ ដល់ ៥ ។',
    'error.card.number.master.first.six.digits.with5' : '៦ ខ្ទង់ដំបូងនៃម៉ាស្ទ័រកាតនេះត្រូវតែស្ថិតនៅចន្លោះ ៥១០០០០ ដល់ ៥៥៩៩៩៩ ។',
    'error.account.emoney.invalid': 'គណនីអ៊ីម៉ាន់នីត្រូវតែជាលេខ',
    'error.account.emoney': 'គណនីអ៊ីម៉ាន់នីមិនត្រឹមត្រូវ',
};

var CONFIG_CN = {
    'error.captcha.empty' : '验证码为空',
    'error.isdn.empty': '电话号码为空',
    'error.isdn.invalid': '电话号码无效',
    'error.isdn.system': '系统很忙。 请稍后再试',
    'error.amount.invalid': '充值金额无效',
    'error.payment_amount.invalid': '付款金额无效',
    'error.card_data.invalid': '卡数据无效',
    'error.payment.type': '请至少选择一种付款方式',
    'error.card.number': '请输入卡号',
    'error.card.name': '请输入持卡人姓名',
    'error.card.cvv': '请输入CVV号码',
    'error.card.expired': '仅以MM / YY格式输入日期',
    'error.card.expired.empty': '请输入到期日',
    'error.captcha.invalid': '请在提交前验证验证码',
    'error.term.invalid': '请查看商定的条款和条件',
    'error.phone.number.invalid': '电话号码必须是号码',
    'error.card.number.invalid': '卡号必须是数字类型',
    'error.topup.amount.number.invalid': '充值金额必须是数字',
    'error.card.cvv.number.invalid': '仅输入三个数字的CVV。',
    'error.topup.number.greater' : '充值金额必须是数字且大于0',
    'error.date.greater.current' : 'Enter date is greater than current date',
    'error.payment.timeout' : '付款流程超时',
    'error.payment.limit': '最大的充值金额：100美元',
    'error.topup.amount.empty': '充值金额为空',
    'error.card.number.visa.length': 'Visa卡号码长度必须为19',
    'error.card.number.visa.fist': '签证卡号码的第一位数必须是4',
    'error.card.number.jcb.length': 'JCB卡号长度必须为16到19',
    'error.card.number.jcb.first' : 'JCB的前4位必须在3528到3589的范围内',
    'error.card.number.master.length': '万事达卡号码长度必须为16',
    'error.card.number.master.first.digit': '万事达卡的第一个数字必须是2或5',
    'error.card.number.master.second.digit.with2' : '该万事达卡的第二位数字必须在2到7之间',
    'error.card.number.master.first.six.digits.with2' : '此万事达卡的前6位数字必须在222100到272099之间',
    'error.card.number.master.second.digit.with5' : '该万事达卡的第二位数字必须在1到5的范围内',
    'error.card.number.master.first.six.digits.with5' : '该万事达卡的前6位数字必须在510000到559999之间',
    'error.account.emoney.invalid': 'eMoney帐户必须为数字',
    'error.account.emoney': 'eMoney帐户无效',

};

var CONFIG_LANG = CONFIG_EN;

$(document).ready(function () {
    switch (LANGUAGE) {
        case CONFIG_LG_TYPE.en:
            CONFIG_LANG = CONFIG_EN;
            break;
        case CONFIG_LG_TYPE.cn:
            CONFIG_LANG = CONFIG_CN;
            break;
        case CONFIG_LG_TYPE.kh:
            CONFIG_LANG = CONFIG_KH;
            break;
        default:
            CONFIG_LANG = CONFIG_EN;
            break;
    }
});


