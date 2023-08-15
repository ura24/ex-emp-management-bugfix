document.addEventListener("DOMContentLoaded", function () {
    const perPage = 10;
    const employeeList = document.querySelector('.employeeList');
    const paginationContainer = document.querySelector('.pagination');
    const totalPages = Math.ceil(employeeList.children.length / perPage);

    // 画面に従業員を10名ずつ表示
    function displayPageItems(page) {
        const startIndex = (page - 1) * perPage;
        const endIndex = startIndex + perPage;

        for (let i = 0; i < employeeList.children.length; i++) {
            if (i >= startIndex && i < endIndex) {
                employeeList.children[i].style.display = '';
            } else {
                employeeList.children[i].style.display = 'none';
            }
        }
    }

    // ページネーションのボタンを設置
    function generatePaginationButtons() {
        paginationContainer.textContent = '';

        for (let i = 1; i <= totalPages; i++) {
            const button = document.createElement('button');
            button.textContent = i;
            button.addEventListener('click', function () {
                displayPageItems(i);
                updateActiveButton(i);
            });
            paginationContainer.appendChild(button);
        }
    }

    // 表示しているページのボタンの色を変更
    function updateActiveButton(activePage) {
        const paginationButtons = paginationContainer.querySelectorAll('button');
        paginationButtons.forEach(button => {
            button.classList.remove('active');
            if (parseInt(button.textContent) === activePage) {
                button.classList.add('active');
            }
        });
    }

    generatePaginationButtons();
    displayPageItems(1);
    updateActiveButton(1);
});
