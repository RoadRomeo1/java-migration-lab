# Indian Tax Engine Standards & Logic (FY 2024-25)

This document outlines the business rules, legal sections, and calculation standards implemented in the Tax Engine.

## 🏗️ Architecture: "Rule-Based Plugin Model"
To ensure the engine is adaptable to future Budget changes (which happen annually in India), we use a decoupled architecture:

1.  **Regime Strategy**: Separate logic blocks for the `OLD` and `NEW` tax regimes.
2.  **Deduction Providers**: Plugins that handle specific deduction types (Standard Deduction, 80C, 80D, etc.).
3.  **Persona-Specific Logic**: Handling presumptive taxation for professionals and business owners separately.

---

## 📋 Standards by Economic Persona

### 1. Full-Time Employee (Salaried)
*   **Income Type**: Salary income.
*   **Standard Deduction (Sec 16ia)**:
    *   **New Regime (FY 24-25)**: ₹75,000 (Upgraded from ₹50,000 in July 2024 Budget).
    *   **Old Regime**: ₹50,000.
*   **Exemptions (Old Regime Only)**: HRA, LTA, Professional Tax.

### 2. Self-Employed (Professionals - Doctors, Consultants)
*   **Income Type**: Profits and Gains of Business or Profession.
*   **Sec 44ADA (Presumptive Taxation)**:
    *   Eligible if turnover < ₹50 Lakhs (or ₹75 Lakhs if 95% is digital).
    *   Calculated as **50% of Total Turnover** as taxable income (assuming 50% expenses).
*   **Deductions**: No separate Standard Deduction is allowed if opting for 44ADA.

### 3. Business Owner (Small Traders/Retailers)
*   **Income Type**: Profits and Gains of Business.
*   **Sec 44AD (Presumptive Taxation)**:
    *   Eligible if turnover < ₹2 Crores.
    *   Calculated as **6% of Turnover** (if received digitally) or **8%** (if cash).
    *   Current Implementation: Fixed at **6% (Modern/Digital focus)**.

---

## 💹 Tax Slab Standards (FY 2024-25)

### New Tax Regime (Default)
| Income Slab (₹) | Rate |
| :--- | :--- |
| Up to 3,00,000 | Nil |
| 3,00,001 - 7,00,000 | 5% |
| 7,00,001 - 10,00,000 | 10% |
| 10,00,001 - 12,00,000 | 15% |
| 12,00,001 - 15,00,000 | 20% |
| Above 15,00,000 | 30% |

*   **Rebate (Sec 87A)**: Available if taxable income ≤ ₹7,00,000. Effectively **NO TAX** for income up to ₹7.75L (including Standard Deduction).

### Old Tax Regime (Optional)
| Income Slab (₹) | Rate |
| :--- | :--- |
| Up to 2,50,000 | Nil |
| 2,50,001 - 5,00,000 | 5% |
| 5,00,001 - 10,00,000 | 20% |
| Above 10,00,000 | 30% |

---

## 🛡️ Surcharges & Cess
*   **Health and Education Cess**: Fixed at **4%** of the Base Tax.
*   **Surcharge**: Applied for high-income earners (usually above ₹50L). *[Next Phase Implementation]*
