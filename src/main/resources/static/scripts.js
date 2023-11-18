function initPage() {
    toggleCheckboxes(true);
    attachListeners();
    updateButtonStates();
    populateImages(false)
}

let lastChecked = null;

function attachListeners() {
    const checkboxes = Array.from(document.querySelectorAll('.action-checkbox'));
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('click', (e) => {
            if (!lastChecked) {
                lastChecked = checkbox;
                return;
            }

            if (e.shiftKey) {
                let start = checkboxes.indexOf(lastChecked);
                let end = checkboxes.indexOf(checkbox);
                checkboxes.slice(Math.min(start, end), Math.max(start, end) + 1)
                    .forEach(cb => cb.checked = lastChecked.checked);
            }

            lastChecked = checkbox;
            updateButtonStates();
        });
    });

    document.addEventListener('keydown', function (event) {
        if (event.key === "Escape") { // "Escape" for modern browsers, "Esc" for IE11
            closeModal();
        }
    });

    window.addEventListener('resize', function() {
        if (document.getElementById('imageModal').style.display === 'block') {
            adjustModalHeight();
        }
    });
}

function toggleCheckboxes(checked) {
    document.querySelectorAll('.action-checkbox').forEach(checkbox => {
        checkbox.checked = checked;
    });
    updateButtonStates();
}

function updateButtonStates() {
    const buttons = document.querySelectorAll('.action-button');
    const anyChecked = Array.from(document.querySelectorAll('.action-checkbox'))
        .some(cb => cb.checked);
    buttons.forEach(button => button.disabled = !anyChecked);
}

function deleteSelectedFiles() {
    const confirmed = confirm("Are you sure you want to delete selected files?");
    if (!confirmed) {
        console.log('Deletion cancelled by the user.');
        return;
    }

    const mainFolderPath = document.getElementById('mainFolderPath').value;
    const checkboxes = document.querySelectorAll('.action-checkbox:checked');
    const filesToDelete = Array.from(checkboxes).map(cb => cb.value);

    fetch('/deleteNonMatchingFiles', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({mainFolderPath, filesToDelete})
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                checkboxes.forEach(cb => cb.closest('tr').remove());
                alert("Selected files have been successfully deleted.");
            } else {
                alert("There was an error deleting the files.");
            }
        })
        .catch(error => console.error('Error:', error));
}

function moveSelectedFiles() {
    const destinationPath = prompt("Enter the destination path for moving files:", "/path/to/destination");
    if (!destinationPath) {
        alert("Destination path not provided. Operation cancelled.");
        return;
    }
    const mainFolderPath = document.getElementById('mainFolderPath').value;
    const checkboxes = document.querySelectorAll('.action-checkbox:checked');
    const filesToMove = Array.from(checkboxes).map(cb => cb.value);

    fetch('/moveFiles', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({mainFolderPath, filesToMove, destinationPath})
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                checkboxes.forEach(cb => cb.closest('tr').remove());
                alert("Selected files have been successfully moved.");
            } else {
                alert("There was an error deleting the files.");
            }
        })
        .catch(error => console.error('Ошибка:', error));
}


function fetchAndDisplayImage(mainFolderPath, filename, imageElementId, callWithConversion, callback) {
    fetch('/images', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({mainFolderPath, filename, callWithConversion})
    })
        .then(response => response.blob())
        .then(blob => {
            const imageURL = URL.createObjectURL(blob);
            document.getElementById(imageElementId).src = imageURL;
            if (callback) {
                callback(blob);
            }
        })
        .catch(error => console.error('Error:', error));
}

function populateImages(callWithConversion) {
    document.querySelectorAll('tr[data-filename]').forEach(row => {
        const mainFolderPath = document.getElementById('mainFolderPath').value;
        const filename = row.getAttribute('data-filename');
        const imageId = 'thumbnail' + row.getAttribute('data-index');

        // Check the file extension
        const isJpg = filename.toLowerCase().endsWith('.jpg');

        // Load JPG files only when callWithConversion is false
        // Load non-JPG files only when callWithConversion is true
        if ((isJpg && !callWithConversion) || (!isJpg && callWithConversion)) {
            fetchAndDisplayImage(mainFolderPath, filename, imageId, callWithConversion);
        }
    });
}

function loadAllThumbnails() {
    // Assuming each row has a data attribute with the filename
    document.querySelectorAll('tr[data-filename]').forEach(row => {
        const mainFolderPath = document.getElementById('mainFolderPath').value;
        const filename = row.getAttribute('data-filename');
        const imageId = 'thumbnail' + row.getAttribute('data-index');
        fetchAndDisplayImage(mainFolderPath, filename, imageId);
    });
}

function loadSpecificThumbnail(element) {
    const mainFolderPath = document.getElementById('mainFolderPath').value;
    const filename = element.getAttribute('data-filename');
    const index = element.getAttribute('data-index');
    const imageId = 'thumbnail' + index;

    fetchAndDisplayImage(mainFolderPath, filename, imageId, true);
}

function openModalWithImage(imageElement) {
    const modalImage = document.getElementById('modalImage');
    const imageModal = document.getElementById('imageModal');

    modalImage.src = imageElement.src;
    imageModal.style.display = 'block';
    adjustModalHeight(); // Adjust modal and image size
}

function adjustModalHeight() {
    const imageModal = document.getElementById('imageModal');
    const viewportHeight = window.innerHeight || document.documentElement.clientHeight;
    imageModal.style.maxHeight = `${viewportHeight * 0.9}px`;
}


function closeModal() {
    document.getElementById('imageModal').style.display = 'none';
}