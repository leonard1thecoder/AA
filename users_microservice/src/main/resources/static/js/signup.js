const form = document.getElementById('signupForm');

// Utility functions
function setError(input, el, message) {
  input.classList.remove('valid');
  input.classList.add('error');
  el.textContent = message;
}
function setValid(input, el) {
  input.classList.remove('error');
  input.classList.add('valid');
  el.textContent = '';
}

// Validation rules for each field
const fields = [
  { id: 'privilege', type: 'number', errorId: 'privilegeError', required: true },
  { id: 'identity', type: 'text', errorId: 'identityError', required: true },
  { id: 'cellphone', type: 'tel', errorId: 'cellphoneError', required: true },
  { id: 'email', type: 'email', errorId: 'emailError', required: true },
  { id: 'password', type: 'password', errorId: 'passwordError', required: true, minLength: 6 },
  { id: 'previousPassword', type: 'password', errorId: 'previousPasswordError', required: false },
  { id: 'fullName', type: 'text', errorId: 'fullNameError', required: true },
  { id: 'age', type: 'number', errorId: 'ageError', required: true },
  { id: 'status', type: 'number', errorId: 'statusError', required: true },
  { id: 'registrationDate', type: 'date', errorId: 'registrationDateError', required: true },
  { id: 'modifiedDate', type: 'date', errorId: 'modifiedDateError', required: true },
  { id: 'passwordUpdateStatus', type: 'number', errorId: 'passwordUpdateStatusError', required: false }
];

// Attach input listeners
fields.forEach(field => {
  const input = document.getElementById(field.id);
  const errorEl = document.getElementById(field.errorId);

  input.addEventListener('input', () => {
    if (field.required && !input.value.trim()) {
      setError(input, errorEl, 'This field is required.');
    } else if (field.minLength && input.value.length < field.minLength) {
      setError(input, errorEl, `Use at least ${field.minLength} characters.`);
    } else if (field.type === 'email' && input.value) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(input.value)) {
        setError(input, errorEl, 'Enter a valid email address.');
        return;
      }
      setValid(input, errorEl);
    } else {
      setValid(input, errorEl);
    }
  });
});

// Handle form submission
form.addEventListener('submit', (e) => {
  e.preventDefault();

  // Trigger validation on all fields
  fields.forEach(field => {
    const input = document.getElementById(field.id);
    input.dispatchEvent(new Event('input'));
  });

  // Check if any errors remain
  const hasErrors = fields.some(field => {
    const input = document.getElementById(field.id);
    return input.classList.contains('error');
  });

  if (hasErrors) return;

  alert('Account created successfully!');
  // TODO: Replace with actual API call:
  // fetch('/api/auth/signup', {
  //   method: 'POST',
  //   headers: { 'Content-Type': 'application/json' },
  //   body: JSON.stringify(Object.fromEntries(new FormData(form)))
  // });
});