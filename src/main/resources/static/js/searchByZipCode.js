const button = document.getElementById("search");
search.addEventListener('click', () => {

    let api = 'https://zipcloud.ibsnet.co.jp/api/search?zipcode=';
    let input = document.getElementById('zipCode');
    let address = document.getElementById('address');
    let param = input.value.replace("-", ""); //入力された郵便番号から「-」を削除
    let url = api + param;

    fetchJsonp(url, {
        timeout: 10000, //タイムアウト時間
    })
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            address.value = data.results[0].address1 + data.results[0].address2 + data.results[0].address3;
        })
        .catch((ex) => { //例外処理
            console.log(ex);
        });
}, false);
