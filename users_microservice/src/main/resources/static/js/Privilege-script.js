// ────────────────────────────────────────────────────────────────
// Add Privilege Form Handler
// ────────────────────────────────────────────────────────────────
document.getElementById('addPrivilegeForm')?.addEventListener('submit', async function(e) {
    e.preventDefault();

    const alertDiv = document.getElementById('privilegeAlert');
    const submitBtn = document.getElementById('submitBtn');

    // Reset UI
    alertDiv.classList.add('d-none');
    alertDiv.classList.remove('alert-success', 'alert-danger');
    alertDiv.textContent = '';
    submitBtn.disabled = true;
    submitBtn.textContent = 'Creating...';

    // Get form values
    const name = document.getElementById('privilegeName').value.trim();

    if (!name) {
        showAlert('Privilege name is required.', 'danger');
        resetButton();
        return;
    }

    // Prepare payload (adjust fields to match your backend DTO)
    const payload = {
        privilegeName: name,
    };

    try {
        const response = await fetch('http://localhost:8081/dev/privileges/api/addPrivilege', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            showAlert('Privilege created successfully!', 'success');
            document.getElementById('addPrivilegeForm').reset(); // clear form
            // Optional: refresh privileges list elsewhere on page
        } else {
            let errorData = null;
            try {
                errorData = await response.json();
            } catch {}

            let message = `Failed to create privilege (${response.status})`;

            if (errorData) {
                if (Array.isArray(errorData) && errorData[0]?.resolveIssueDetails) {
                    message = errorData[0].resolveIssueDetails;
                } else if (errorData.resolveIssueDetails) {
                    message = errorData.resolveIssueDetails;
                } else if (errorData.message) {
                    message = errorData.message;
                } else if (errorData.detail) {
                    message = errorData.detail;
                }
            }

            showAlert(message, 'danger');
        }
    } catch (err) {
        console.error('Error:', err);
        showAlert('Network error – please check if the server is running.', 'danger');
    } finally {
        resetButton();
    }

    function showAlert(text, type) {
        alertDiv.textContent = text;
        alertDiv.classList.add(`alert-${type}`);
        alertDiv.classList.remove('d-none');
    }

    function resetButton() {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Create Privilege';
    }
});