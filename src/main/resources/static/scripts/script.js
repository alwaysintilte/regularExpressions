// script.js
class RegexValidator {
    constructor() {
        this.form = document.getElementById('regexForm');
        this.patternInput = document.getElementById('pattern');
        this.textInput = document.getElementById('text');
        this.validateBtn = document.getElementById('validateBtn');
        this.resultDiv = document.getElementById('result');

        this.initEventListeners();
    }

    initEventListeners() {
        this.form.addEventListener('submit', (e) => {
            e.preventDefault();
            this.validate();
        });

        // Очистка результатов при изменении входных данных
        [this.patternInput, this.textInput].forEach(input => {
            input.addEventListener('input', () => {
                this.hideResult();
            });
        });
    }

    async validate() {
        const pattern = this.patternInput.value.trim();
        const text = this.textInput.value;

        // Базовая валидация на клиенте
        if (!pattern) {
            this.showResult('error', 'Ошибка: Регулярное выражение не может быть пустым');
            return;
        }

        this.setLoading(true);

        try {
            const response = await fetch('/api/validate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    pattern: pattern,
                    text: text
                })
            });

            if (!response.ok) {
                throw new Error('Ошибка сети');
            }

            const result = await response.json();
            this.displayResult(result);

        } catch (error) {
            console.error('Error:', error);
            this.showResult('error', 'Произошла ошибка при проверке. Попробуйте еще раз.');
        } finally {
            this.setLoading(false);
        }
    }

    displayResult(result) {
        if (result.success) {
            if (result.matches && result.matches.length > 0) {
                this.showMatchesResult(result);
            } else {
                this.showResult('warning', result.message);
            }
        } else {
            this.showResult('error', result.message);
        }
    }

    showMatchesResult(result) {
        const matches = result.matches;
        const highlightedText = result.highlightedText || this.textInput.value;

        let html = `
            <h3>✅ ${result.message}</h3>
            <div class="matches-count">Найдено совпадений: ${matches.length}</div>

            <div class="matches-list">
                <h4>Детали совпадений:</h4>
        `;

        matches.forEach((match, index) => {
            html += `
                <div class="match-item">
                    <strong>#${index + 1}:</strong> Позиция ${match.start}-${match.end} →
                    "<code>${this.escapeHtml(match.matchedText)}</code>"
                </div>
            `;
        });

        html += `
            </div>

            <div class="highlighted-text">
                <h4>Текст с выделенными совпадениями:</h4>
                <div>${highlightedText}</div>
            </div>
        `;

        this.showResult('success', html, true);
    }

    showResult(type, message, isHtml = false) {
        this.resultDiv.className = `result ${type}`;

        if (isHtml) {
            this.resultDiv.innerHTML = message;
        } else {
            this.resultDiv.innerHTML = `<h3>${message}</h3>`;
        }

        this.resultDiv.style.display = 'block';
        this.scrollToResult();
    }

    hideResult() {
        this.resultDiv.style.display = 'none';
    }

    scrollToResult() {
        this.resultDiv.scrollIntoView({
            behavior: 'smooth',
            block: 'nearest'
        });
    }

    setLoading(loading) {
        const btnText = this.validateBtn.querySelector('.btn-text');
        const btnLoading = this.validateBtn.querySelector('.btn-loading');

        if (loading) {
            btnText.style.display = 'none';
            btnLoading.style.display = 'inline';
            this.validateBtn.disabled = true;
            this.form.classList.add('loading');
        } else {
            btnText.style.display = 'inline';
            btnLoading.style.display = 'none';
            this.validateBtn.disabled = false;
            this.form.classList.remove('loading');
        }
    }

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', () => {
    new RegexValidator();
});

// Добавляем обработчик для демонстрационных данных
document.addEventListener('DOMContentLoaded', () => {
    // Примеры для быстрого тестирования
    const examples = [
        { pattern: 'a.b', text: 'acb adb aeb' },
        { pattern: 'test.*', text: 'test123 testing test' },
        { pattern: '\\d+', text: 'abc 123 def 456' },
        { pattern: 'hello', text: 'Hello world hello there' }
    ];

    // Можно добавить кнопки для примеров, если нужно
    console.log('Regex Validator loaded. Examples:', examples);
});