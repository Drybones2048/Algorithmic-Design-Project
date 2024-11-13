import subprocess
import time
import random
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import os


class ProjectBenchmark:
    def __init__(self, java_dir: str):
        self.java_dir = java_dir
        self.results = pd.DataFrame()

        # Define appropriate size ranges for each program based on their complexity
        self.program_sizes = {
            'Dynamic.Program3': [15, 30, 45, 60],  # Naive solution - very small inputs
            'Dynamic.Program4': [1000, 2000, 3000, 4000, 5000],  # O(n³) solution - medium inputs
            'Dynamic.Program5A': [1000, 2000, 3000, 4000, 5000],  # O(n²) solution - large inputs
            'Dynamic.Program5B': [1000, 2000, 3000, 4000, 5000]  # O(n²) solution - large inputs
        }

        self.trials = 5  # Number of trials for each size

    def generate_test_case(self, n: int) -> str:
        W = random.randint(max(10, n // 2), n * 2)
        heights = [random.randint(1, 10 ** 4) for _ in range(n)]
        widths = [random.randint(1, min(W - 1, 10 ** 4)) for _ in range(n)]

        input_str = f"{n} {W}\n"
        input_str += " ".join(map(str, heights)) + "\n"
        input_str += " ".join(map(str, widths)) + "\n"
        return input_str

    def run_single_test(self, program_name: str, input_str: str) -> float:
        try:
            start_time = time.time()
            process = subprocess.run(
                ['java', '-cp', self.java_dir, program_name],
                input=input_str.encode(),
                capture_output=True,
                timeout=60  # 1 minute timeout
            )
            end_time = time.time()

            if process.returncode != 0:
                print(f"Error running {program_name}: {process.stderr.decode()}")
                return None

            return end_time - start_time

        except subprocess.TimeoutExpired:
            print(f"Timeout for {program_name}")
            return None

    def run_benchmarks(self):
        results = []

        for program_name, sizes in self.program_sizes.items():
            print(f"\nTesting {program_name}")

            for size in sizes:
                print(f"  Size {size}")

                trial_times = []
                for trial in range(self.trials):
                    print(f"    Trial {trial + 1}/{self.trials}")
                    input_str = self.generate_test_case(size)
                    time_taken = self.run_single_test(program_name, input_str)

                    if time_taken is not None:
                        trial_times.append(time_taken)

                if trial_times:
                    avg_time = np.mean(trial_times)
                    std_dev = np.std(trial_times)
                    print(f"    Average time: {avg_time:.3f}s ± {std_dev:.3f}s")

                    results.append({
                        'size': size,
                        'program': program_name.split('.')[-1],
                        'time': avg_time,
                        'std_dev': std_dev
                    })

        self.results = pd.DataFrame(results)

    def create_plot(self, programs_to_include=None, title=None, filename=None):
        plt.figure(figsize=(12, 8))

        programs = programs_to_include if programs_to_include else self.results['program'].unique()

        for program in sorted(programs):
            program_data = self.results[self.results['program'] == program]
            if not program_data.empty:
                plt.errorbar(
                    program_data['size'],
                    program_data['time'],
                    yerr=program_data['std_dev'],
                    label=program,
                    marker='o'
                )

        plt.xlabel('Input Size (n)')
        plt.ylabel('Execution Time (seconds)')
        plt.title(title if title else 'Program Performance Comparison')
        plt.legend()
        plt.grid(True)

        if filename:
            plt.savefig(filename)
        plt.close()


def main():
    # Setup
    current_dir = os.getcwd()
    java_dir = os.path.join(current_dir, "out")

    print("Starting benchmark suite...")
    print(f"Java directory: {java_dir}")

    # Create and run benchmarks
    benchmark = ProjectBenchmark(java_dir)
    benchmark.run_benchmarks()

    # Create all required plots
    print("\nGenerating plots...")

    # Plot3: Program3 only (with note about small input sizes)
    benchmark.create_plot(
        programs_to_include=['Program3'],
        title='Program3 Performance (Naive Solution)\nNote: Using smaller input sizes due to complexity',
        filename='plot3_program3.png'
    )

    # Plot4: Program4 only
    benchmark.create_plot(
        programs_to_include=['Program4'],
        title='Program4 Performance (O(n³) Solution)',
        filename='plot4_program4.png'
    )

    # Plot5: Program5A only
    benchmark.create_plot(
        programs_to_include=['Program5A'],
        title='Program5A Performance (Top-down Dynamic Programming)',
        filename='plot5_program5a.png'
    )

    # Plot6: Program5B only
    benchmark.create_plot(
        programs_to_include=['Program5B'],
        title='Program5B Performance (Bottom-up Dynamic Programming)',
        filename='plot6_program5b.png'
    )

    # Plot7: All programs
    # Note: This plot will show different ranges for different algorithms
    benchmark.create_plot(
        title='All Programs Performance Comparison\nNote: Different input size ranges used based on algorithm complexity',
        filename='plot7_all_programs.png'
    )

    # Plot8: Program5A vs Program5B
    benchmark.create_plot(
        programs_to_include=['Program5A', 'Program5B'],
        title='Top-down vs Bottom-up Dynamic Programming Comparison',
        filename='plot8_5a_5b_comparison.png'
    )

    # Save numerical results
    benchmark.results.to_csv('benchmark_results.csv', index=False)

    print("\nBenchmarking complete! Results saved to:")
    print("- benchmark_results.csv")
    print("- plot3_program3.png through plot8_5a_5b_comparison.png")


if __name__ == "__main__":
    main()