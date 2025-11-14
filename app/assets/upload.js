

(function() {
const refreshTime = 1000;
let refreshInterval = null;

function refreshUploadStatus() {
    fetch('/ngr-physical-frontend/uploaded-status-fragment')
        .then(response => response.text())
        .then(html => {
            document.getElementById('uploadStatusTable').innerHTML = html;
        })
        .catch(err => console.error())

    const dataList = document.querySelector('dl');
    const statuses = Array.from(dataList.querySelectorAll('dd')).map(dd => dd.textContent.trim());

    if(!statuses.includes('Uploading')) {
        continueButton = document.getElementById('continue')
        form = continueButton.form;
        form.action = form.action.replace(true, false);
        continueButton.disabled = false;
        clearInterval(refreshInterval);

    }

}

refreshUploadStatus();
refreshInterval = setInterval(refreshUploadStatus, refreshTime);
})();