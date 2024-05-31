---


---

<h1 id="developer-diary">Developer Diary</h1>
<h2 id="date-2024-03-14">[Date: 2024-03-14]</h2>
<h3 id="todays-tasks">Today’s Tasks</h3>
<ul>
<li>Task 1: Simulate and serialize sine wave generation in Java, which later can be integrated into Android Studio directly.</li>
</ul>
<h3 id="detailed-notes">Detailed Notes</h3>
<h4 id="task-1-simulate-sine-wave-generation-in-java">Task 1: [Simulate Sine Wave Generation in Java]</h4>
<p>Today’s primary task involved generating a sine wave in Java. This simulation will be useful for later integration into an Android application. The process includes calculating sine wave values, storing them in a list, and then serializing this list for later use.</p>
<h5 id="steps">Steps:</h5>
<ul>
<li>Defined key parameters for the sine wave: sample size, frequency, and amplitude.</li>
<li>Utilized a loop to compute the sine wave values at discrete time intervals.</li>
<li>Created a list to store instances of a custom class <code>SineWaveData</code>, which holds the time and corresponding sine wave value.</li>
<li>Implemented a method to serialize the list of <code>SineWaveData</code> objects and save it to a file named <code>sineWaveDataList.ser</code>.</li>
</ul>
<p>Here’s the implementation in Java:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">package</span> wave_test<span class="token punctuation">;</span>

<span class="token keyword">import</span> java<span class="token punctuation">.</span>io<span class="token punctuation">.</span>FileOutputStream<span class="token punctuation">;</span>
<span class="token keyword">import</span> java<span class="token punctuation">.</span>io<span class="token punctuation">.</span>IOException<span class="token punctuation">;</span>
<span class="token keyword">import</span> java<span class="token punctuation">.</span>io<span class="token punctuation">.</span>ObjectOutputStream<span class="token punctuation">;</span>
<span class="token keyword">import</span> java<span class="token punctuation">.</span>util<span class="token punctuation">.</span>ArrayList<span class="token punctuation">;</span>
<span class="token keyword">import</span> java<span class="token punctuation">.</span>util<span class="token punctuation">.</span>List<span class="token punctuation">;</span>

<span class="token keyword">public</span> <span class="token keyword">class</span> <span class="token class-name">Main</span> <span class="token punctuation">{</span>

    <span class="token keyword">public</span> <span class="token keyword">static</span> <span class="token keyword">void</span> <span class="token function">main</span><span class="token punctuation">(</span>String<span class="token punctuation">[</span><span class="token punctuation">]</span> args<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">final</span> <span class="token keyword">int</span> sampleSize <span class="token operator">=</span> <span class="token number">100</span><span class="token punctuation">;</span>
        <span class="token keyword">final</span> <span class="token keyword">double</span> frequency <span class="token operator">=</span> <span class="token number">5</span><span class="token punctuation">;</span>
        <span class="token keyword">final</span> <span class="token keyword">double</span> amplitude <span class="token operator">=</span> <span class="token number">1</span><span class="token punctuation">;</span>
        <span class="token keyword">final</span> <span class="token keyword">double</span> twoPiF <span class="token operator">=</span> <span class="token number">2</span> <span class="token operator">*</span> Math<span class="token punctuation">.</span>PI <span class="token operator">*</span> frequency<span class="token punctuation">;</span>
        List<span class="token operator">&lt;</span>SineWaveData<span class="token operator">&gt;</span> dataList <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">ArrayList</span><span class="token operator">&lt;</span><span class="token operator">&gt;</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">// Create a list to store data points</span>

        <span class="token keyword">for</span> <span class="token punctuation">(</span><span class="token keyword">int</span> i <span class="token operator">=</span> <span class="token number">0</span><span class="token punctuation">;</span> i <span class="token operator">&lt;</span> sampleSize<span class="token punctuation">;</span> i<span class="token operator">++</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
            <span class="token keyword">double</span> time <span class="token operator">=</span> i <span class="token operator">/</span> <span class="token punctuation">(</span><span class="token keyword">double</span><span class="token punctuation">)</span> sampleSize<span class="token punctuation">;</span>
            <span class="token keyword">double</span> sineValue <span class="token operator">=</span> amplitude <span class="token operator">*</span> Math<span class="token punctuation">.</span><span class="token function">sin</span><span class="token punctuation">(</span>twoPiF <span class="token operator">*</span> time<span class="token punctuation">)</span><span class="token punctuation">;</span>
            SineWaveData data <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">SineWaveData</span><span class="token punctuation">(</span>time<span class="token punctuation">,</span> sineValue<span class="token punctuation">)</span><span class="token punctuation">;</span>
            dataList<span class="token punctuation">.</span><span class="token function">add</span><span class="token punctuation">(</span>data<span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">// Add into the list</span>
        <span class="token punctuation">}</span>

        <span class="token function">serializeSineWaveData</span><span class="token punctuation">(</span>dataList<span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">// serialize the whole list</span>
    <span class="token punctuation">}</span>

    <span class="token keyword">private</span> <span class="token keyword">static</span> <span class="token keyword">void</span> <span class="token function">serializeSineWaveData</span><span class="token punctuation">(</span>List<span class="token operator">&lt;</span>SineWaveData<span class="token operator">&gt;</span> dataList<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">try</span> <span class="token punctuation">{</span>
            FileOutputStream fileOut <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">FileOutputStream</span><span class="token punctuation">(</span><span class="token string">"sineWaveDataList.ser"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            ObjectOutputStream out <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">ObjectOutputStream</span><span class="token punctuation">(</span>fileOut<span class="token punctuation">)</span><span class="token punctuation">;</span>
            out<span class="token punctuation">.</span><span class="token function">writeObject</span><span class="token punctuation">(</span>dataList<span class="token punctuation">)</span><span class="token punctuation">;</span> 
            out<span class="token punctuation">.</span><span class="token function">close</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            fileOut<span class="token punctuation">.</span><span class="token function">close</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            System<span class="token punctuation">.</span>out<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span><span class="token string">"Serialized data is saved in sineWaveDataList.ser"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">IOException</span> i<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            i<span class="token punctuation">.</span><span class="token function">printStackTrace</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre>
<h3 id="summary">Summary</h3>
<p>Today’s tasks focused on simulating and serializing sine wave data in Java. By defining key parameters, calculating sine wave values, and storing these values in a list of <code>SineWaveData</code> objects, we successfully serialized the data to a file. This serialized data can later be deserialized and used in an Android application, ensuring smooth integration and real-time data processing. The implementation ensures that the data is accurately calculated and stored, ready for future use.</p>

