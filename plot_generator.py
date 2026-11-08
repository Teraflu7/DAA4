import pandas as pd
import matplotlib.pyplot as plt
import os

os.makedirs('results/plots', exist_ok=True)
df = pd.read_csv('results/results.csv')

# Plot 1: Edit Distance Time (Log Scale)
plt.figure(figsize=(8, 5))
dp_df = df[df['family'] == 'EditDistance']
for alg in ['Table', 'TwoRow', 'Recursive']:
    sub = dp_df[(dp_df['algorithm'] == alg) & (dp_df['input_type'] == 'worst_case_diff')]
    if not sub.empty:
        plt.plot(sub['n'], sub['time_ms'], marker='o', label=alg)

plt.yscale('log')
plt.title('Plot 1: Edit Distance Execution Time (Log Scale)')
plt.xlabel('String Length (n)')
plt.ylabel('Time (ms) [Log Scale]')
plt.grid(True, which="both", ls="--", alpha=0.5)
plt.legend()
plt.tight_layout()
plt.savefig('results/plots/plot1_edit_distance_time.png', dpi=300)
plt.close()

# Plot 2: String Search Comparisons in Worst Case
plt.figure(figsize=(8, 5))
search_wc = df[(df['family'] == 'StringSearch') & (df['input_type'] == 'worst_case')]
for alg in ['Naive', 'KMP']:
    sub = search_wc[search_wc['algorithm'] == alg]
    plt.plot(sub['n'], sub['counter_value'], marker='s', label=alg)

plt.title('Plot 2: Character Comparisons in Worst-Case Scenario')
plt.xlabel('Text Length (n) [characters]')
plt.ylabel('Character Comparisons (count)')
plt.grid(True, ls="--", alpha=0.5)
plt.legend()
plt.tight_layout()
plt.savefig('results/plots/plot2_string_search_worst_case.png', dpi=300)
plt.close()

# Plot 3: Ratio of Comparisons (Naive / KMP)
plt.figure(figsize=(8, 5))
naive_sub = search_wc[search_wc['algorithm'] == 'Naive'].sort_values('n')
kmp_sub = search_wc[search_wc['algorithm'] == 'KMP'].sort_values('n')

ratios = naive_sub['counter_value'].values / kmp_sub['counter_value'].values
plt.plot(naive_sub['n'], ratios, marker='^', color='crimson', linewidth=2)
plt.title('Plot 3: Comparison Ratio (Naive / KMP) on Worst-Case Input')
plt.xlabel('Text Length (n)')
plt.ylabel('Ratio (Naive Comparisons / KMP Comparisons)')
plt.grid(True, ls="--", alpha=0.5)
plt.tight_layout()
plt.savefig('results/plots/plot3_string_search_ratio.png', dpi=300)
plt.close()

print("Plots successfully generated in results/plots/")