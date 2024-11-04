// validation.js
document.addEventListener("DOMContentLoaded", function() {
    const forms = document.querySelectorAll("form");

    forms.forEach(form => {
        form.addEventListener("submit", function(event) {
            const ageInput = document.getElementById("age");
            const sexInput = document.getElementById("sex");

            const age = parseInt(ageInput.value, 10);
            const sex = parseInt(sexInput.value, 10);
            let isValid = true;

            // 年齢のバリデーション（10刻みであること）
            if (age < 0 || age % 10 !== 0) {
                alert("年齢は0以上の10刻みの数値でなければなりません。");
                isValid = false;
            }

            // 性別のバリデーション（0または1であること）
            if (sex !== 0 && sex !== 1) {
                alert("性別は0(男)または1(女)でなければなりません。");
                isValid = false;
            }

            // バリデーションに失敗した場合はフォーム送信をキャンセル
            if (!isValid) {
                event.preventDefault(); // フォームの送信をキャンセル
            }
        });
    });
});
