// Open the modal
function openCreateCategoryModal() {
    document.getElementById('createCategoryModal').style.display = 'block';
}

// Validate the form
function validateCreateCategoryForm() {
    // Validate the category name input and description input
    const categoryNameInput = document.getElementById('categoryName');
    const categoryName = categoryNameInput.value.trim();
    if (categoryName.length === 0) {
        // get the error message element and set the error message
        const errorMessageElement = document.getElementById('categoryNameError');
        errorMessageElement.innerText = 'Name is required';
        errorMessageElement.style.display = 'block';
        categoryNameInput.focus();
        return false;
    } else {
        // get the error message element and hide it
        const errorMessageElement = document.getElementById('categoryNameError');
        errorMessageElement.style.display = 'none';
    }

    if (categoryName.length < 3 || categoryName.length > 255) {
        // get the error message element and set the error message
        const errorMessageElement = document.getElementById('categoryNameError');
        errorMessageElement.innerText = 'Name must be between 3 and 255 characters';
        errorMessageElement.style.display = 'block';
        categoryNameInput.focus();
        return false;
    } else {
        // get the error message element and hide it
        const errorMessageElement = document.getElementById('categoryNameError');
        errorMessageElement.style.display = 'none';
    }

    const categoryDescriptionInput = document.getElementById('categoryDescription');
    const categoryDescription = categoryDescriptionInput.value.trim();

    if (categoryDescription.length > 500) {
        // get the error message element and set the error message
        const errorMessageElement = document.getElementById('categoryDescriptionError');
        errorMessageElement.innerText = 'Description must be less than 500 characters';
        errorMessageElement.style.display = 'block';
        categoryDescriptionInput.focus();
        return false;
    } else {
        // get the error message element and hide it
        const errorMessageElement = document.getElementById('categoryDescriptionError');
        errorMessageElement.style.display = 'none';
    }

    return true;
}

function createCategory() {
    let valid = validateCreateCategoryForm();

    if (!valid) {
        return;
    }

    // // Using fetch API to send a request to the server to create a new category
    // fetch('/categories/api/create', {
    //     method: 'POST',
    //     headers: {
    //         'Content-Type': 'application/json'
    //     },
    //     body: JSON.stringify({
    //         name: document.getElementById('categoryName').value,
    //         description: document.getElementById('categoryDescription').value
    //     })
    // }).then(response => {
    //     if (response.ok && response.body != null) {
    //         return response.json();
    //     }
    //     throw new Error('Failed to create a new category');
    // }).then(data => {
    //     // Append the new category to the select element
    //     const categorySelect = document.getElementById('category');
    //     const newOption = document.createElement('option');
    //     newOption.value = data.id;
    //     newOption.text = data.name;
    //     categorySelect.add(newOption);

    //     // Set the new category as the selected option
    //     categorySelect.value = data.id;

    //     // Close the modal
    //     closeModal();
    // }).catch(error => {
    //     console.error(error);
    // });

    let name = document.getElementById('categoryName').value;
    let description = document.getElementById('categoryDescription').value;

    // var xhttp = new XMLHttpRequest();
    // xhttp.onreadystatechange = function () {
    //     if (this.readyState == 4 && this.status == 200) {
    //         const data = JSON.parse(xhttp.responseText);
    //         // Append the new category to the select element
    //         const categorySelect = document.getElementById('category');
    //         const newOption = document.createElement('option');
    //         newOption.value = data.id;
    //         newOption.text = data.name;
    //         categorySelect.add(newOption);

    //         // Set the new category as the selected option
    //         categorySelect.value = data.id;

    //         // Close the modal
    //         closeModal();
    //     }
    // };
    // xhttp.open("post", "http://localhost:8080/categories/api/create", true);
    // xhttp.setRequestHeader("Content-type", "application/json");
    // xhttp.send(JSON.stringify({
    //     name: name,
    //     description: description
    // }));

    const data = JSON.stringify({
        name: name,
        description: description
    });

    $.ajax({
        url: "http://localhost:8080/categories/api/create",
        type: 'POST',
        data: data,
        headers: {
            'Content-Type': 'application/json'
        },
        dataType: 'json',
        success: function (result) {
            // Append the new category to the select element
            const categorySelect = document.getElementById('category');
            const newOption = document.createElement('option');
            newOption.value = result.id;
            newOption.text = result.name;
            categorySelect.add(newOption);

            // Set the new category as the selected option
            categorySelect.value = result.id;

            // Close the modal
            closeModal();
        },
        error: function (error) {
            console.error(error);
        }
    });
}

// Close the modal
function closeModal() {
    document.getElementById('createCategoryModal').style.display = 'none';
}