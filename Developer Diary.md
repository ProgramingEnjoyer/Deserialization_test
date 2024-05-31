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
<h2 id="date-2024-03-15">[Date: 2024-03-15]</h2>
<h3 id="todays-tasks-1">Today’s Tasks</h3>
<ul>
<li>Task 1: Deserialize sine wave data in Java and verify the deserialized data.</li>
</ul>
<h3 id="detailed-notes-1">Detailed Notes</h3>
<h4 id="task-1-deserialize-sine-wave-data-in-java">Task 1: [Deserialize Sine Wave Data in Java]</h4>
<p>Today’s primary task involved deserializing the previously serialized sine wave data in Java. This step is crucial for reading the sine wave data back into the application, allowing for verification and potential further processing.</p>
<h5 id="steps-1">Steps:</h5>
<ul>
<li>Opened the serialized file <code>sineWaveDataList.ser</code> to read the objects list.</li>
<li>Deserialized the list of <code>SineWaveData</code> objects.</li>
<li>Printed each deserialized object’s information to verify the data.</li>
</ul>
<p>Here’s the implementation in Java:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">package</span> wave_test<span class="token punctuation">;</span>

<span class="token keyword">import</span> java<span class="token punctuation">.</span>io<span class="token punctuation">.</span>FileInputStream<span class="token punctuation">;</span>
<span class="token keyword">import</span> java<span class="token punctuation">.</span>io<span class="token punctuation">.</span>ObjectInputStream<span class="token punctuation">;</span>
<span class="token keyword">import</span> java<span class="token punctuation">.</span>io<span class="token punctuation">.</span>IOException<span class="token punctuation">;</span>
<span class="token keyword">import</span> java<span class="token punctuation">.</span>util<span class="token punctuation">.</span>List<span class="token punctuation">;</span>

<span class="token keyword">public</span> <span class="token keyword">class</span> <span class="token class-name">DeserializeSineWave</span> <span class="token punctuation">{</span>
    <span class="token keyword">public</span> <span class="token keyword">static</span> <span class="token keyword">void</span> <span class="token function">main</span><span class="token punctuation">(</span>String<span class="token punctuation">[</span><span class="token punctuation">]</span> args<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">try</span> <span class="token punctuation">{</span>
            <span class="token comment">// Open the .ser file to read the objects list</span>
            FileInputStream fileIn <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">FileInputStream</span><span class="token punctuation">(</span><span class="token string">"sineWaveDataList.ser"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            ObjectInputStream in <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">ObjectInputStream</span><span class="token punctuation">(</span>fileIn<span class="token punctuation">)</span><span class="token punctuation">;</span>
            
            <span class="token comment">// Read and deserialize the objects list</span>
            List<span class="token operator">&lt;</span>SineWaveData<span class="token operator">&gt;</span> dataList <span class="token operator">=</span> <span class="token punctuation">(</span>List<span class="token operator">&lt;</span>SineWaveData<span class="token operator">&gt;</span><span class="token punctuation">)</span> in<span class="token punctuation">.</span><span class="token function">readObject</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            
            <span class="token comment">// Close the input streams</span>
            in<span class="token punctuation">.</span><span class="token function">close</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            fileIn<span class="token punctuation">.</span><span class="token function">close</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            
            <span class="token comment">// Print out each deserialized object's information to verify data</span>
            System<span class="token punctuation">.</span>out<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span><span class="token string">"Deserialized SineWaveData List..."</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            <span class="token keyword">for</span> <span class="token punctuation">(</span>SineWaveData data <span class="token operator">:</span> dataList<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                System<span class="token punctuation">.</span>out<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span><span class="token string">"Time: "</span> <span class="token operator">+</span> data<span class="token punctuation">.</span><span class="token function">getTime</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">+</span> <span class="token string">", Sine Value: "</span> <span class="token operator">+</span> data<span class="token punctuation">.</span><span class="token function">getSineValue</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            <span class="token punctuation">}</span>
            
        <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">IOException</span> i<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            i<span class="token punctuation">.</span><span class="token function">printStackTrace</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            <span class="token keyword">return</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">ClassNotFoundException</span> c<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            System<span class="token punctuation">.</span>out<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span><span class="token string">"SineWaveData class or List not found"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            c<span class="token punctuation">.</span><span class="token function">printStackTrace</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            <span class="token keyword">return</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>

<span class="token comment">// Class to hold sine wave data points</span>
<span class="token keyword">class</span> <span class="token class-name">SineWaveData</span> <span class="token keyword">implements</span> <span class="token class-name">java<span class="token punctuation">.</span>io<span class="token punctuation">.</span>Serializable</span> <span class="token punctuation">{</span>
    <span class="token keyword">private</span> <span class="token keyword">static</span> <span class="token keyword">final</span> <span class="token keyword">long</span> serialVersionUID <span class="token operator">=</span> 1L<span class="token punctuation">;</span>
    <span class="token keyword">private</span> <span class="token keyword">double</span> time<span class="token punctuation">;</span>
    <span class="token keyword">private</span> <span class="token keyword">double</span> value<span class="token punctuation">;</span>

    <span class="token keyword">public</span> <span class="token function">SineWaveData</span><span class="token punctuation">(</span><span class="token keyword">double</span> time<span class="token punctuation">,</span> <span class="token keyword">double</span> value<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">this</span><span class="token punctuation">.</span>time <span class="token operator">=</span> time<span class="token punctuation">;</span>
        <span class="token keyword">this</span><span class="token punctuation">.</span>value <span class="token operator">=</span> value<span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

    <span class="token keyword">public</span> <span class="token keyword">double</span> <span class="token function">getTime</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">return</span> time<span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

    <span class="token keyword">public</span> <span class="token keyword">double</span> <span class="token function">getSineValue</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">return</span> value<span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>

## <span class="token punctuation">[</span>Date<span class="token operator">:</span> <span class="token number">2024</span><span class="token operator">-</span><span class="token number">03</span><span class="token operator">-</span><span class="token number">18</span><span class="token punctuation">]</span>

### Today's Tasks

<span class="token operator">-</span>   Task <span class="token number">1</span><span class="token operator">:</span> Initialize and check Bluetooth support in the Android application<span class="token punctuation">.</span>
<span class="token operator">-</span>   Task <span class="token number">2</span><span class="token operator">:</span> Implement Bluetooth device discovery and manage connections<span class="token punctuation">.</span>
<span class="token operator">-</span>   Task <span class="token number">3</span><span class="token operator">:</span> Integrate data deserialization and display in the Android application<span class="token punctuation">.</span>

### Detailed Notes

#### Task <span class="token number">1</span><span class="token operator">:</span> <span class="token punctuation">[</span>Initialize and Check Bluetooth Support<span class="token punctuation">]</span>

Today<span class="token string">'s primary task involved initializing and checking Bluetooth support in the Android application. This included ensuring the device supports Bluetooth, enabling it if it'</span>s not already enabled<span class="token punctuation">,</span> and preparing the application <span class="token keyword">for</span> Bluetooth operations<span class="token punctuation">.</span>

##### Steps<span class="token operator">:</span>

<span class="token operator">-</span>   Checked <span class="token keyword">if</span> the device supports Bluetooth<span class="token punctuation">.</span>
<span class="token operator">-</span>   Requested the user to enable Bluetooth <span class="token keyword">if</span> it was not already enabled<span class="token punctuation">.</span>
<span class="token operator">-</span>   Registered a BroadcastReceiver to listen <span class="token keyword">for</span> newly discovered Bluetooth devices<span class="token punctuation">.</span>

Here’s a snippet of the implementation<span class="token operator">:</span>
```java
<span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">checkBluetoothSupport</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token keyword">if</span> <span class="token punctuation">(</span>bluetoothAdapter <span class="token operator">==</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        Toast<span class="token punctuation">.</span><span class="token function">makeText</span><span class="token punctuation">(</span><span class="token keyword">this</span><span class="token punctuation">,</span> <span class="token string">"Bluetooth is not supported on this device"</span><span class="token punctuation">,</span> Toast<span class="token punctuation">.</span>LENGTH_SHORT<span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">show</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token function">finish</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">// End application</span>
    <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token keyword">if</span> <span class="token punctuation">(</span><span class="token operator">!</span>bluetoothAdapter<span class="token punctuation">.</span><span class="token function">isEnabled</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        Intent enableBtIntent <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">Intent</span><span class="token punctuation">(</span>BluetoothAdapter<span class="token punctuation">.</span>ACTION_REQUEST_ENABLE<span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token function">startActivityForResult</span><span class="token punctuation">(</span>enableBtIntent<span class="token punctuation">,</span> REQUEST_ENABLE_BT<span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre>
<h4 id="task-2-implement-bluetooth-device-discovery-and-manage-connections">Task 2: [Implement Bluetooth Device Discovery and Manage Connections]</h4>
<p>The second task focused on discovering nearby Bluetooth devices and managing connections with selected devices.</p>
<h5 id="steps-2">Steps:</h5>
<ul>
<li>Started device discovery upon user request.</li>
<li>Displayed a list of discovered devices in a dialog.</li>
<li>Managed connections using threads for connecting and disconnecting from devices.</li>
</ul>
<p>Here’s a snippet of the implementation:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token annotation punctuation">@SuppressLint</span><span class="token punctuation">(</span><span class="token string">"MissingPermission"</span><span class="token punctuation">)</span>
<span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">discoverDevices</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token keyword">if</span> <span class="token punctuation">(</span>bluetoothAdapter<span class="token punctuation">.</span><span class="token function">isDiscovering</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        bluetoothAdapter<span class="token punctuation">.</span><span class="token function">cancelDiscovery</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
    bluetoothAdapter<span class="token punctuation">.</span><span class="token function">startDiscovery</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>

<span class="token annotation punctuation">@SuppressLint</span><span class="token punctuation">(</span><span class="token string">"MissingPermission"</span><span class="token punctuation">)</span>
<span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">showDeviceListDialog</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
    AlertDialog<span class="token punctuation">.</span>Builder builder <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">AlertDialog<span class="token punctuation">.</span>Builder</span><span class="token punctuation">(</span>MainActivity<span class="token punctuation">.</span><span class="token keyword">this</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    View dialogView <span class="token operator">=</span> LayoutInflater<span class="token punctuation">.</span><span class="token function">from</span><span class="token punctuation">(</span>MainActivity<span class="token punctuation">.</span><span class="token keyword">this</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">inflate</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>layout<span class="token punctuation">.</span>bsheet_bluetooth<span class="token punctuation">,</span> null<span class="token punctuation">)</span><span class="token punctuation">;</span>
    builder<span class="token punctuation">.</span><span class="token function">setView</span><span class="token punctuation">(</span>dialogView<span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token comment">// Initialization and setup for Bluetooth device selection</span>
    <span class="token comment">// ...</span>
    
    AlertDialog dialog <span class="token operator">=</span> builder<span class="token punctuation">.</span><span class="token function">create</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    ListView listView <span class="token operator">=</span> dialogView<span class="token punctuation">.</span><span class="token function">findViewById</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>id<span class="token punctuation">.</span>device_list<span class="token punctuation">)</span><span class="token punctuation">;</span>
    listView<span class="token punctuation">.</span><span class="token function">setAdapter</span><span class="token punctuation">(</span>devicesArrayAdapter<span class="token punctuation">)</span><span class="token punctuation">;</span>
    listView<span class="token punctuation">.</span><span class="token function">setOnItemClickListener</span><span class="token punctuation">(</span><span class="token punctuation">(</span>parent<span class="token punctuation">,</span> view<span class="token punctuation">,</span> position<span class="token punctuation">,</span> id<span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token punctuation">{</span>
        selectedBluetoothDevice <span class="token operator">=</span> bluetoothDevices<span class="token punctuation">.</span><span class="token function">get</span><span class="token punctuation">(</span>position<span class="token punctuation">)</span><span class="token punctuation">;</span>
        btDeviceTextView<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Selected device: "</span> <span class="token operator">+</span> selectedBluetoothDevice<span class="token punctuation">.</span><span class="token function">getName</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    btnConnect<span class="token punctuation">.</span><span class="token function">setOnClickListener</span><span class="token punctuation">(</span>v <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token punctuation">{</span>
        <span class="token keyword">if</span> <span class="token punctuation">(</span>isConnected<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            <span class="token keyword">new</span> <span class="token class-name">DisconnectThread</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">start</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
            <span class="token keyword">if</span> <span class="token punctuation">(</span>selectedBluetoothDevice <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                <span class="token keyword">new</span> <span class="token class-name">ConnectThread</span><span class="token punctuation">(</span>selectedBluetoothDevice<span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">start</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            <span class="token punctuation">}</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    btnCancel<span class="token punctuation">.</span><span class="token function">setOnClickListener</span><span class="token punctuation">(</span>v <span class="token operator">-</span><span class="token operator">&gt;</span> dialog<span class="token punctuation">.</span><span class="token function">dismiss</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    dialog<span class="token punctuation">.</span><span class="token function">show</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre>
<h4 id="task-3-integrate-data-deserialization-and-display">Task 3: [Integrate Data Deserialization and Display]</h4>
<p>The third task was to integrate data deserialization and display it in the Android application. This involved loading serialized sine wave data from a file and displaying it in a custom view.</p>
<h5 id="steps-3">Steps:</h5>
<ul>
<li>Implemented functionality to request necessary permissions for accessing external storage.</li>
<li>Opened a file picker to select the serialized data file.</li>
<li>Deserialized the sine wave data and displayed it in a custom view.</li>
</ul>
<p>Here’s a snippet of the implementation:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">loadData</span><span class="token punctuation">(</span>Uri fileUri<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token keyword">try</span> <span class="token punctuation">{</span>
        FileInputStream fileIn <span class="token operator">=</span> <span class="token punctuation">(</span>FileInputStream<span class="token punctuation">)</span> <span class="token function">getContentResolver</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">openInputStream</span><span class="token punctuation">(</span>fileUri<span class="token punctuation">)</span><span class="token punctuation">;</span>
        ObjectInputStream in <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">ObjectInputStream</span><span class="token punctuation">(</span>fileIn<span class="token punctuation">)</span><span class="token punctuation">;</span>

        List<span class="token operator">&lt;</span>SineWaveData<span class="token operator">&gt;</span> dataList <span class="token operator">=</span> <span class="token punctuation">(</span>List<span class="token operator">&lt;</span>SineWaveData<span class="token operator">&gt;</span><span class="token punctuation">)</span> in<span class="token punctuation">.</span><span class="token function">readObject</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        in<span class="token punctuation">.</span><span class="token function">close</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        fileIn<span class="token punctuation">.</span><span class="token function">close</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

        <span class="token comment">// Display data in custom view</span>
        SineWaveView sineWaveView <span class="token operator">=</span> <span class="token function">findViewById</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>id<span class="token punctuation">.</span>sineWaveView<span class="token punctuation">)</span><span class="token punctuation">;</span>
        sineWaveView<span class="token punctuation">.</span><span class="token function">setDataList</span><span class="token punctuation">(</span>dataList<span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">FileNotFoundException</span> e<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        Log<span class="token punctuation">.</span><span class="token function">e</span><span class="token punctuation">(</span><span class="token string">"MainActivity"</span><span class="token punctuation">,</span> <span class="token string">"File not found"</span><span class="token punctuation">,</span> e<span class="token punctuation">)</span><span class="token punctuation">;</span>
        Toast<span class="token punctuation">.</span><span class="token function">makeText</span><span class="token punctuation">(</span><span class="token function">getApplicationContext</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">,</span> <span class="token string">"File not found"</span><span class="token punctuation">,</span> Toast<span class="token punctuation">.</span>LENGTH_SHORT<span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">show</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">IOException</span> e<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        Log<span class="token punctuation">.</span><span class="token function">e</span><span class="token punctuation">(</span><span class="token string">"MainActivity"</span><span class="token punctuation">,</span> <span class="token string">"IO Exception"</span><span class="token punctuation">,</span> e<span class="token punctuation">)</span><span class="token punctuation">;</span>
        Toast<span class="token punctuation">.</span><span class="token function">makeText</span><span class="token punctuation">(</span><span class="token function">getApplicationContext</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">,</span> <span class="token string">"IO Exception"</span><span class="token punctuation">,</span> Toast<span class="token punctuation">.</span>LENGTH_SHORT<span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">show</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">ClassNotFoundException</span> e<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        Log<span class="token punctuation">.</span><span class="token function">e</span><span class="token punctuation">(</span><span class="token string">"MainActivity"</span><span class="token punctuation">,</span> <span class="token string">"Class not found"</span><span class="token punctuation">,</span> e<span class="token punctuation">)</span><span class="token punctuation">;</span>
        Toast<span class="token punctuation">.</span><span class="token function">makeText</span><span class="token punctuation">(</span><span class="token function">getApplicationContext</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">,</span> <span class="token string">"Class not found"</span><span class="token punctuation">,</span> Toast<span class="token punctuation">.</span>LENGTH_SHORT<span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">show</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre>
<h3 id="summary">Summary</h3>
<p>Today’s tasks focused on enhancing the Android application with Bluetooth support and data deserialization functionality. The Bluetooth initialization and discovery process were set up.</p>
<h2 id="date-2024-03-19">[Date: 2024-03-19]</h2>
<h3 id="todays-tasks-2">Today’s Tasks</h3>
<ul>
<li>Task 1: Manage data transmission after successful Bluetooth connection.</li>
<li>Task 2: Implement user-initiated disconnection and resource cleanup.</li>
<li>Task 3: Handle errors and provide clear user guidance.</li>
</ul>
<h3 id="detailed-notes-2">Detailed Notes</h3>
<h4 id="task-1-manage-data-transmission-after-successful-bluetooth-connection">Task 1: [Manage Data Transmission After Successful Bluetooth Connection]</h4>
<p>Today’s primary task involved managing data transmission after a successful Bluetooth connection. This included creating input/output streams and handling these streams on separate threads to avoid blocking the UI thread.</p>
<h5 id="steps-4">Steps:</h5>
<ul>
<li>Created input and output streams from the Bluetooth socket.</li>
<li>Implemented separate threads for reading from and writing to the streams.</li>
<li>Ensured continuous data handling without blocking the main UI thread.</li>
</ul>
<p>Here’s a snippet of the implementation:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">private</span> <span class="token keyword">class</span> <span class="token class-name">ConnectedThread</span> <span class="token keyword">extends</span> <span class="token class-name">Thread</span> <span class="token punctuation">{</span>
    <span class="token keyword">private</span> <span class="token keyword">final</span> BluetoothSocket mmSocket<span class="token punctuation">;</span>
    <span class="token keyword">private</span> <span class="token keyword">final</span> InputStream mmInStream<span class="token punctuation">;</span>
    <span class="token keyword">private</span> <span class="token keyword">final</span> OutputStream mmOutStream<span class="token punctuation">;</span>

    <span class="token keyword">public</span> <span class="token function">ConnectedThread</span><span class="token punctuation">(</span>BluetoothSocket socket<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        mmSocket <span class="token operator">=</span> socket<span class="token punctuation">;</span>
        InputStream tmpIn <span class="token operator">=</span> null<span class="token punctuation">;</span>
        OutputStream tmpOut <span class="token operator">=</span> null<span class="token punctuation">;</span>

        <span class="token keyword">try</span> <span class="token punctuation">{</span>
            tmpIn <span class="token operator">=</span> socket<span class="token punctuation">.</span><span class="token function">getInputStream</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            tmpOut <span class="token operator">=</span> socket<span class="token punctuation">.</span><span class="token function">getOutputStream</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">IOException</span> e<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            Log<span class="token punctuation">.</span><span class="token function">e</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Error occurred when creating input/output stream"</span><span class="token punctuation">,</span> e<span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span>

        mmInStream <span class="token operator">=</span> tmpIn<span class="token punctuation">;</span>
        mmOutStream <span class="token operator">=</span> tmpOut<span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

    <span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">run</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">byte</span><span class="token punctuation">[</span><span class="token punctuation">]</span> buffer <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">byte</span><span class="token punctuation">[</span><span class="token number">1024</span><span class="token punctuation">]</span><span class="token punctuation">;</span>
        <span class="token keyword">int</span> numBytes<span class="token punctuation">;</span>

        <span class="token keyword">while</span> <span class="token punctuation">(</span><span class="token boolean">true</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
            <span class="token keyword">try</span> <span class="token punctuation">{</span>
                numBytes <span class="token operator">=</span> mmInStream<span class="token punctuation">.</span><span class="token function">read</span><span class="token punctuation">(</span>buffer<span class="token punctuation">)</span><span class="token punctuation">;</span>
                <span class="token comment">// Handle the received bytes</span>
                <span class="token comment">// For example, update UI or process data</span>
            <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">IOException</span> e<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                Log<span class="token punctuation">.</span><span class="token function">d</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Input stream was disconnected"</span><span class="token punctuation">,</span> e<span class="token punctuation">)</span><span class="token punctuation">;</span>
                <span class="token keyword">break</span><span class="token punctuation">;</span>
            <span class="token punctuation">}</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span>

    <span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">write</span><span class="token punctuation">(</span><span class="token keyword">byte</span><span class="token punctuation">[</span><span class="token punctuation">]</span> bytes<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">try</span> <span class="token punctuation">{</span>
            mmOutStream<span class="token punctuation">.</span><span class="token function">write</span><span class="token punctuation">(</span>bytes<span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">IOException</span> e<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            Log<span class="token punctuation">.</span><span class="token function">e</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Error occurred when sending data"</span><span class="token punctuation">,</span> e<span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span>

    <span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">cancel</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">try</span> <span class="token punctuation">{</span>
            mmSocket<span class="token punctuation">.</span><span class="token function">close</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">IOException</span> e<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            Log<span class="token punctuation">.</span><span class="token function">e</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Could not close the connect socket"</span><span class="token punctuation">,</span> e<span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre>
<h4 id="task-2-implement-user-initiated-disconnection-and-resource-cleanup">Task 2: [Implement User-Initiated Disconnection and Resource Cleanup]</h4>
<p>The second task focused on implementing a way for the user to actively disconnect from the Bluetooth device and ensure all resources are properly released.</p>
<h5 id="steps-5">Steps:</h5>
<ul>
<li>Implemented a button to allow the user to disconnect from the Bluetooth device.</li>
<li>Ensured the socket and input/output streams are closed when the user disconnects.</li>
<li>Unregistered broadcast receivers and released other resources upon application destruction to prevent memory leaks.</li>
</ul>
<p>Here’s a snippet of the implementation:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">private</span> <span class="token keyword">class</span> <span class="token class-name">DisconnectThread</span> <span class="token keyword">extends</span> <span class="token class-name">Thread</span> <span class="token punctuation">{</span>
    <span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">run</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">try</span> <span class="token punctuation">{</span>
            <span class="token keyword">if</span> <span class="token punctuation">(</span>bluetoothSocket <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                bluetoothSocket<span class="token punctuation">.</span><span class="token function">close</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                bluetoothSocket <span class="token operator">=</span> null<span class="token punctuation">;</span>
                <span class="token function">runOnUiThread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token punctuation">{</span>
                    <span class="token keyword">if</span> <span class="token punctuation">(</span>btnConnect <span class="token operator">!=</span> null <span class="token operator">&amp;&amp;</span> btDeviceTextView <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                        btnConnect<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Connect"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                        btDeviceTextView<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Please select a device"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                        isConnected <span class="token operator">=</span> <span class="token boolean">false</span><span class="token punctuation">;</span> <span class="token comment">// Update connection status</span>
                    <span class="token punctuation">}</span>
                <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            <span class="token punctuation">}</span>
        <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">IOException</span> e<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            Log<span class="token punctuation">.</span><span class="token function">e</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Could not close the client socket"</span><span class="token punctuation">,</span> e<span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>

<span class="token annotation punctuation">@Override</span>
<span class="token keyword">protected</span> <span class="token keyword">void</span> <span class="token function">onDestroy</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token keyword">super</span><span class="token punctuation">.</span><span class="token function">onDestroy</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token comment">// Unregister discovery broadcast receiver</span>
    <span class="token function">unregisterReceiver</span><span class="token punctuation">(</span>receiver<span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token comment">// Unregister Bluetooth connection state change broadcast receiver</span>
    <span class="token function">unregisterReceiver</span><span class="token punctuation">(</span>mReceiver<span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre>
<h4 id="task-3-handle-errors-and-provide-clear-user-guidance">Task 3: [Handle Errors and Provide Clear User Guidance]</h4>
<p>The third task was to handle errors and provide clear guidance to the user on how to resolve issues, such as permission requests and error messages.</p>
<h5 id="steps-6">Steps:</h5>
<ul>
<li>Implemented error handling for Bluetooth operations.</li>
<li>Displayed clear error messages to the user and guided them to resolve issues.</li>
<li>Added checks for necessary permissions and provided instructions to the user for granting permissions.</li>
</ul>
<p>Here’s a snippet of the implementation:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token annotation punctuation">@Override</span>
<span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">onRequestPermissionsResult</span><span class="token punctuation">(</span><span class="token keyword">int</span> requestCode<span class="token punctuation">,</span> <span class="token annotation punctuation">@NonNull</span> String<span class="token punctuation">[</span><span class="token punctuation">]</span> permissions<span class="token punctuation">,</span> <span class="token annotation punctuation">@NonNull</span> <span class="token keyword">int</span><span class="token punctuation">[</span><span class="token punctuation">]</span> grantResults<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token keyword">super</span><span class="token punctuation">.</span><span class="token function">onRequestPermissionsResult</span><span class="token punctuation">(</span>requestCode<span class="token punctuation">,</span> permissions<span class="token punctuation">,</span> grantResults<span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token keyword">if</span> <span class="token punctuation">(</span>requestCode <span class="token operator">==</span> REQUEST_BLUETOOTH_SCAN_PERMISSION<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">if</span> <span class="token punctuation">(</span>grantResults<span class="token punctuation">.</span>length <span class="token operator">&gt;</span> <span class="token number">0</span> <span class="token operator">&amp;&amp;</span> grantResults<span class="token punctuation">[</span><span class="token number">0</span><span class="token punctuation">]</span> <span class="token operator">==</span> PackageManager<span class="token punctuation">.</span>PERMISSION_GRANTED<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            <span class="token comment">// Permission granted, continue with Bluetooth scanning</span>
        <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
            <span class="token comment">// Permission denied, explain to the user</span>
            Toast<span class="token punctuation">.</span><span class="token function">makeText</span><span class="token punctuation">(</span><span class="token keyword">this</span><span class="token punctuation">,</span> <span class="token string">"Bluetooth scan permission is required to discover devices"</span><span class="token punctuation">,</span> Toast<span class="token punctuation">.</span>LENGTH_SHORT<span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">show</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>

<span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">showErrorDialog</span><span class="token punctuation">(</span>String message<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token keyword">new</span> <span class="token class-name">AlertDialog<span class="token punctuation">.</span>Builder</span><span class="token punctuation">(</span><span class="token keyword">this</span><span class="token punctuation">)</span>
        <span class="token punctuation">.</span><span class="token function">setTitle</span><span class="token punctuation">(</span><span class="token string">"Error"</span><span class="token punctuation">)</span>
        <span class="token punctuation">.</span><span class="token function">setMessage</span><span class="token punctuation">(</span>message<span class="token punctuation">)</span>
        <span class="token punctuation">.</span><span class="token function">setPositiveButton</span><span class="token punctuation">(</span>android<span class="token punctuation">.</span>R<span class="token punctuation">.</span>string<span class="token punctuation">.</span>ok<span class="token punctuation">,</span> null<span class="token punctuation">)</span>
        <span class="token punctuation">.</span><span class="token function">show</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre>
<h3 id="summary-1">Summary</h3>
<p>Today’s tasks focused on enhancing the Bluetooth connectivity features of the Android application. Data transmission was handled efficiently using separate threads for input and output streams. User-initiated disconnection was implemented, ensuring that resources are properly released and the application remains responsive. Additionally, robust error handling and clear user guidance were added to improve the user experience and address potential issues proactively.</p>
<h2 id="date-2024-03-21">[Date: 2024-03-21]</h2>
<h3 id="todays-tasks-3">Today’s Tasks</h3>
<ul>
<li>Task 1: Design the UI for discovering and connecting Bluetooth devices.</li>
<li>Task 2: Integrate the UI design into the existing Bluetooth connection logic.</li>
</ul>
<h3 id="detailed-notes-3">Detailed Notes</h3>
<h4 id="task-1-design-the-ui-for-discovering-and-connecting-bluetooth-devices">Task 1: [Design the UI for Discovering and Connecting Bluetooth Devices]</h4>
<p>Today’s primary task involved designing a user interface (UI) for discovering and connecting to Bluetooth devices. This UI allows users to see available devices, initiate connections, and handle ongoing connection states.</p>
<h5 id="steps-7">Steps:</h5>
<ul>
<li>Created a layout file <code>bluetooth_device_list.xml</code> to define the UI components for device discovery and connection.</li>
<li>Included elements such as an ImageView for the Bluetooth logo, a TextView for device selection, a ProgressBar for indicating ongoing operations, a ListView for displaying available devices, and buttons for canceling and connecting to devices.</li>
</ul>
<p>Here’s the layout of UI:<br>
<img src="https://photos.app.goo.gl/i4PeUWFkCkrZ1FEa9" alt="UI of device list"></p>
<h4 id="task-2-integrate-the-ui-design-into-the-existing-bluetooth-connection-logic">Task 2: [Integrate the UI Design into the Existing Bluetooth Connection Logic]</h4>
<p>The second task focused on integrating the designed UI into the existing Bluetooth connection logic. This included updating the UI based on the connection status and user actions.</p>
<h5 id="steps-8">Steps:</h5>
<ul>
<li>Updated the <code>MainActivity</code> to inflate the new layout and handle user interactions.</li>
<li>Managed the visibility and text of the ProgressBar, TextView, and Buttons based on the Bluetooth connection status.</li>
<li>Updated the logic for displaying the list of discovered devices and handling user selections.</li>
</ul>
<p>Here’s a snippet of the integration in <code>MainActivity</code>:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">showDeviceListDialog</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
    AlertDialog<span class="token punctuation">.</span>Builder builder <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">AlertDialog<span class="token punctuation">.</span>Builder</span><span class="token punctuation">(</span>MainActivity<span class="token punctuation">.</span><span class="token keyword">this</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    View dialogView <span class="token operator">=</span> LayoutInflater<span class="token punctuation">.</span><span class="token function">from</span><span class="token punctuation">(</span>MainActivity<span class="token punctuation">.</span><span class="token keyword">this</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">inflate</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>layout<span class="token punctuation">.</span>bluetooth_device_list<span class="token punctuation">,</span> null<span class="token punctuation">)</span><span class="token punctuation">;</span>
    builder<span class="token punctuation">.</span><span class="token function">setView</span><span class="token punctuation">(</span>dialogView<span class="token punctuation">)</span><span class="token punctuation">;</span>

    btnConnect <span class="token operator">=</span> dialogView<span class="token punctuation">.</span><span class="token function">findViewById</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>id<span class="token punctuation">.</span>bt_connect<span class="token punctuation">)</span><span class="token punctuation">;</span>
    btnCancel <span class="token operator">=</span> dialogView<span class="token punctuation">.</span><span class="token function">findViewById</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>id<span class="token punctuation">.</span>bt_cancel<span class="token punctuation">)</span><span class="token punctuation">;</span>
    btDeviceTextView <span class="token operator">=</span> dialogView<span class="token punctuation">.</span><span class="token function">findViewById</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>id<span class="token punctuation">.</span>btDevice<span class="token punctuation">)</span><span class="token punctuation">;</span>
    ProgressBar btProgress <span class="token operator">=</span> dialogView<span class="token punctuation">.</span><span class="token function">findViewById</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>id<span class="token punctuation">.</span>bt_progress<span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token comment">// Initially hide the progress bar</span>
    btProgress<span class="token punctuation">.</span><span class="token function">setVisibility</span><span class="token punctuation">(</span>View<span class="token punctuation">.</span>INVISIBLE<span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token comment">// Update the TextView and Button based on connection status</span>
    <span class="token keyword">if</span> <span class="token punctuation">(</span>isConnected <span class="token operator">&amp;&amp;</span> selectedBluetoothDevice <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        btDeviceTextView<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Connected device: "</span> <span class="token operator">+</span> selectedBluetoothDevice<span class="token punctuation">.</span><span class="token function">getName</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token keyword">if</span> <span class="token punctuation">(</span>selectedBluetoothDevice <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        btDeviceTextView<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Selected device: "</span> <span class="token operator">+</span> selectedBluetoothDevice<span class="token punctuation">.</span><span class="token function">getName</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
        btDeviceTextView<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Please select a device"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

    btnConnect<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span>isConnected <span class="token operator">?</span> <span class="token string">"Disconnect"</span> <span class="token operator">:</span> <span class="token string">"Connect"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    AlertDialog dialog <span class="token operator">=</span> builder<span class="token punctuation">.</span><span class="token function">create</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    ListView listView <span class="token operator">=</span> dialogView<span class="token punctuation">.</span><span class="token function">findViewById</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>id<span class="token punctuation">.</span>device_list<span class="token punctuation">)</span><span class="token punctuation">;</span>
    listView<span class="token punctuation">.</span><span class="token function">setAdapter</span><span class="token punctuation">(</span>devicesArrayAdapter<span class="token punctuation">)</span><span class="token punctuation">;</span>
    listView<span class="token punctuation">.</span><span class="token function">setOnItemClickListener</span><span class="token punctuation">(</span><span class="token punctuation">(</span>parent<span class="token punctuation">,</span> view<span class="token punctuation">,</span> position<span class="token punctuation">,</span> id<span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token punctuation">{</span>
        selectedBluetoothDevice <span class="token operator">=</span> bluetoothDevices<span class="token punctuation">.</span><span class="token function">get</span><span class="token punctuation">(</span>position<span class="token punctuation">)</span><span class="token punctuation">;</span>
        btDeviceTextView<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Selected device: "</span> <span class="token operator">+</span> selectedBluetoothDevice<span class="token punctuation">.</span><span class="token function">getName</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    btnConnect<span class="token punctuation">.</span><span class="token function">setOnClickListener</span><span class="token punctuation">(</span>v <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token punctuation">{</span>
        <span class="token keyword">if</span> <span class="token punctuation">(</span>isConnected<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            <span class="token keyword">new</span> <span class="token class-name">DisconnectThread</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">start</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
            <span class="token keyword">if</span> <span class="token punctuation">(</span>selectedBluetoothDevice <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                btProgress<span class="token punctuation">.</span><span class="token function">setVisibility</span><span class="token punctuation">(</span>View<span class="token punctuation">.</span>VISIBLE<span class="token punctuation">)</span><span class="token punctuation">;</span>
                <span class="token keyword">new</span> <span class="token class-name">ConnectThread</span><span class="token punctuation">(</span>selectedBluetoothDevice<span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">start</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            <span class="token punctuation">}</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    btnCancel<span class="token punctuation">.</span><span class="token function">setOnClickListener</span><span class="token punctuation">(</span>v <span class="token operator">-</span><span class="token operator">&gt;</span> dialog<span class="token punctuation">.</span><span class="token function">dismiss</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    dialog<span class="token punctuation">.</span><span class="token function">show</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre>
<h3 id="summary-2">Summary</h3>
<p>Today’s tasks focused on designing and integrating a user-friendly UI for discovering and connecting Bluetooth devices. The new UI provides a clear and intuitive interface for users to manage their Bluetooth connections.</p>
<h2 id="date-2024-03-22">[Date: 2024-03-22]</h2>
<h3 id="todays-tasks-4">Today’s Tasks</h3>
<ul>
<li>Task 1: Set up and test a rotary encoder on Arduino.</li>
<li>Task 2: Implement signal simulation on Arduino using the rotary encoder.</li>
<li>Task 3: Transmit simulated signals from Arduino to an Android phone via HC-05.</li>
</ul>
<h3 id="detailed-notes-4">Detailed Notes</h3>
<h4 id="task-1-set-up-and-test-rotary-encoder-on-arduino">Task 1: [Set Up and Test Rotary Encoder on Arduino]</h4>
<p>Today’s primary task involved setting up and testing a rotary encoder on an Arduino. This included wiring the encoder to the Arduino and initializing the necessary pins.</p>
<h5 id="steps-9">Steps:</h5>
<ul>
<li>Connected the rotary encoder to Arduino pins: CLK (pin 4), DT (pin 6), and SW (pin 5).</li>
<li>Configured the pins in the <code>setup</code> function.</li>
<li>Initialized the Bluetooth connection using the HC-05 module on pins 10 (RX) and 11 (TX).</li>
</ul>
<p>Here’s the setup function implementation:</p>
<pre class=" language-c"><code class="prism ++ language-c"><span class="token keyword">void</span> <span class="token function">setup</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
  Serial<span class="token punctuation">.</span><span class="token function">begin</span><span class="token punctuation">(</span><span class="token number">9600</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
  myBluetooth<span class="token punctuation">.</span><span class="token function">begin</span><span class="token punctuation">(</span><span class="token number">9600</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

  <span class="token function">pinMode</span><span class="token punctuation">(</span>clkPin<span class="token punctuation">,</span> INPUT<span class="token punctuation">)</span><span class="token punctuation">;</span>
  <span class="token function">pinMode</span><span class="token punctuation">(</span>dtPin<span class="token punctuation">,</span> INPUT<span class="token punctuation">)</span><span class="token punctuation">;</span>
  <span class="token function">pinMode</span><span class="token punctuation">(</span>swPin<span class="token punctuation">,</span> INPUT_PULLUP<span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">// Set the button as input with internal pull-up resistor</span>

  <span class="token comment">// Initialize all the readings to 0:</span>
  <span class="token keyword">for</span> <span class="token punctuation">(</span><span class="token keyword">int</span> thisReading <span class="token operator">=</span> <span class="token number">0</span><span class="token punctuation">;</span> thisReading <span class="token operator">&lt;</span> numReadings<span class="token punctuation">;</span> thisReading<span class="token operator">++</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
    readings<span class="token punctuation">[</span>thisReading<span class="token punctuation">]</span> <span class="token operator">=</span> <span class="token number">0</span><span class="token punctuation">;</span>
  <span class="token punctuation">}</span>

  previousStateCLK <span class="token operator">=</span> <span class="token function">digitalRead</span><span class="token punctuation">(</span>clkPin<span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre>
<h4 id="task-2-implement-signal-simulation-on-arduino-using-the-rotary-encoder">Task 2: [Implement Signal Simulation on Arduino Using the Rotary Encoder]</h4>
<p>The second task focused on simulating signals using the rotary encoder and processing the encoder’s output with a moving average filter.</p>
<h5 id="steps-10">Steps:</h5>
<ul>
<li>Read the encoder’s state changes to calculate the raw encoder value.</li>
<li>Applied a moving average filter to smooth the raw encoder values.</li>
<li>Implemented a debounce mechanism for the encoder button to reset the values.</li>
</ul>
<p>Here’s the loop function implementation:</p>
<pre class=" language-c"><code class="prism ++ language-c"><span class="token keyword">void</span> <span class="token function">loop</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
  currentStateCLK <span class="token operator">=</span> <span class="token function">digitalRead</span><span class="token punctuation">(</span>clkPin<span class="token punctuation">)</span><span class="token punctuation">;</span>
  buttonState <span class="token operator">=</span> <span class="token function">digitalRead</span><span class="token punctuation">(</span>swPin<span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">// Read the button state</span>

  <span class="token comment">// Check if button state has changed to LOW (button pressed)</span>
  <span class="token keyword">if</span> <span class="token punctuation">(</span>buttonState <span class="token operator">==</span> LOW <span class="token operator">&amp;&amp;</span> lastButtonState <span class="token operator">==</span> HIGH<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    rawEncoderValue <span class="token operator">=</span> <span class="token number">0</span><span class="token punctuation">;</span> 
    encoderValue <span class="token operator">=</span> <span class="token number">0</span><span class="token punctuation">;</span> 
    <span class="token comment">// Debounce delay to avoid accidental quick presses</span>
    <span class="token function">delay</span><span class="token punctuation">(</span><span class="token number">50</span><span class="token punctuation">)</span><span class="token punctuation">;</span> 
  <span class="token punctuation">}</span>
  <span class="token keyword">unsigned</span> <span class="token keyword">long</span> currentMillis <span class="token operator">=</span> <span class="token function">millis</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">// Get the current time</span>
  <span class="token keyword">if</span> <span class="token punctuation">(</span>currentStateCLK <span class="token operator">!=</span> previousStateCLK<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    previousMillis <span class="token operator">=</span> currentMillis<span class="token punctuation">;</span>

    <span class="token keyword">if</span> <span class="token punctuation">(</span><span class="token function">digitalRead</span><span class="token punctuation">(</span>dtPin<span class="token punctuation">)</span> <span class="token operator">!=</span> currentStateCLK<span class="token punctuation">)</span> <span class="token punctuation">{</span>
      rawEncoderValue<span class="token operator">++</span><span class="token punctuation">;</span> 
    <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
      rawEncoderValue<span class="token operator">--</span><span class="token punctuation">;</span> 
    <span class="token punctuation">}</span>

    <span class="token comment">// Update the moving average filter</span>
    total <span class="token operator">=</span> total <span class="token operator">-</span> readings<span class="token punctuation">[</span>readIndex<span class="token punctuation">]</span><span class="token punctuation">;</span>
    readings<span class="token punctuation">[</span>readIndex<span class="token punctuation">]</span> <span class="token operator">=</span> rawEncoderValue<span class="token punctuation">;</span>
    total <span class="token operator">=</span> total <span class="token operator">+</span> readings<span class="token punctuation">[</span>readIndex<span class="token punctuation">]</span><span class="token punctuation">;</span>
    readIndex <span class="token operator">=</span> readIndex <span class="token operator">+</span> <span class="token number">1</span><span class="token punctuation">;</span>

    <span class="token keyword">if</span> <span class="token punctuation">(</span>readIndex <span class="token operator">&gt;=</span> numReadings<span class="token punctuation">)</span> <span class="token punctuation">{</span>
      readIndex <span class="token operator">=</span> <span class="token number">0</span><span class="token punctuation">;</span> <span class="token comment">// ...wrap around to the beginning:</span>
    <span class="token punctuation">}</span>

    <span class="token comment">// Calculate the average:</span>
    encoderValue <span class="token operator">=</span> total <span class="token operator">/</span> numReadings<span class="token punctuation">;</span>

    <span class="token comment">// Send serialized data over Bluetooth</span>
    myBluetooth<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span><span class="token string">"{ \"time\": "</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    myBluetooth<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span>currentMillis<span class="token punctuation">)</span><span class="token punctuation">;</span>
    myBluetooth<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span><span class="token string">", \"value\": "</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    myBluetooth<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span>encoderValue<span class="token punctuation">)</span><span class="token punctuation">;</span>
    myBluetooth<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span><span class="token string">" }"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token comment">// Also print to Serial Monitor</span>
    Serial<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span><span class="token string">"{ \"time\": "</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    Serial<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span>currentMillis<span class="token punctuation">)</span><span class="token punctuation">;</span>
    Serial<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span><span class="token string">", \"value\": "</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    Serial<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span>encoderValue<span class="token punctuation">)</span><span class="token punctuation">;</span>
    Serial<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span><span class="token string">" }"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    lastEncoderTime <span class="token operator">=</span> currentMillis<span class="token punctuation">;</span> <span class="token comment">// Update the last encoder time</span>
  <span class="token punctuation">}</span>

  previousStateCLK <span class="token operator">=</span> currentStateCLK<span class="token punctuation">;</span>
  lastButtonState <span class="token operator">=</span> buttonState<span class="token punctuation">;</span> <span class="token comment">// Update the last button state</span>
  <span class="token function">delay</span><span class="token punctuation">(</span><span class="token number">1</span><span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">// A short delay to reduce noise</span>
<span class="token punctuation">}</span>
</code></pre>
<h4 id="task-3-transmit-simulated-signals-from-arduino-to-android-phone-via-hc-05">Task 3: [Transmit Simulated Signals from Arduino to Android Phone via HC-05]</h4>
<p>The third task involved transmitting the simulated signals from the Arduino to an Android phone using the HC-05 Bluetooth module.</p>
<h5 id="steps-11">Steps:</h5>
<ul>
<li>Set up the HC-05 Bluetooth module for communication.</li>
<li>Serialized the data as JSON and sent it over Bluetooth.</li>
<li>Displayed the transmitted data on the Arduino Serial Monitor for verification.</li>
</ul>
<p>Key parts of the transmission implementation:</p>
<pre class=" language-c"><code class="prism ++ language-c"><span class="token comment">// Send serialized data over Bluetooth</span>
myBluetooth<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span><span class="token string">"{ \"time\": "</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
myBluetooth<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span>currentMillis<span class="token punctuation">)</span><span class="token punctuation">;</span>
myBluetooth<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span><span class="token string">", \"value\": "</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
myBluetooth<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span>encoderValue<span class="token punctuation">)</span><span class="token punctuation">;</span>
myBluetooth<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span><span class="token string">" }"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

<span class="token comment">// Also print to Serial Monitor</span>
Serial<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span><span class="token string">"{ \"time\": "</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
Serial<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span>currentMillis<span class="token punctuation">)</span><span class="token punctuation">;</span>
Serial<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span><span class="token string">", \"value\": "</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
Serial<span class="token punctuation">.</span><span class="token function">print</span><span class="token punctuation">(</span>encoderValue<span class="token punctuation">)</span><span class="token punctuation">;</span>
Serial<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span><span class="token string">" }"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
</code></pre>
<h3 id="summary-3">Summary</h3>
<p>Today’s tasks focused on setting up and testing a rotary encoder on Arduino, simulating signals using the encoder, and transmitting these signals to an Android phone via the HC-05 Bluetooth module. The integration of a moving average filter helped in smoothing out the encoder values, providing a more stable output for transmission.</p>
<h2 id="date-2024-03-23">[Date: 2024-03-23]</h2>
<h3 id="todays-tasks-5">Today’s Tasks</h3>
<ul>
<li>Task 1: Conduct connectivity tests between Arduino and Android device.</li>
<li>Task 2: Debug and resolve common connectivity issues.</li>
<li>Task 3: Ensure reliable real-time transmission of rotary encoder data.</li>
</ul>
<h3 id="detailed-notes-5">Detailed Notes</h3>
<h4 id="task-1-conduct-connectivity-tests-between-arduino-and-android-device">Task 1: [Conduct Connectivity Tests Between Arduino and Android Device]</h4>
<p>Today’s primary task involved testing the connectivity between the Arduino and the Android device using the HC-05 Bluetooth module. This included verifying that data is being sent from the Arduino and received correctly on the Android device.</p>
<h5 id="steps-12">Steps:</h5>
<ul>
<li>Established a Bluetooth connection between the Arduino and Android device.</li>
<li>Verified data transmission from the Arduino to the Android device.</li>
<li>Monitored the data received on the Android application for accuracy.</li>
</ul>
<h4 id="task-2-debug-and-resolve-common-connectivity-issues">Task 2: [Debug and Resolve Common Connectivity Issues]</h4>
<p>During the connectivity tests, several common issues were identified and resolved. Here are some potential bugs and their solutions:</p>
<h5 id="bug-1-bluetooth-connection-failure">Bug 1: Bluetooth Connection Failure</h5>
<ul>
<li><strong>Symptom:</strong> The Bluetooth connection between the Arduino and Android device fails intermittently.</li>
<li><strong>Solution:</strong> Ensure the Bluetooth module is properly powered and within range. Add retries in the connection logic.</li>
</ul>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">connectToBluetoothDevice</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token keyword">new</span> <span class="token class-name">Thread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token punctuation">{</span>
        <span class="token keyword">boolean</span> success <span class="token operator">=</span> <span class="token boolean">false</span><span class="token punctuation">;</span>
        <span class="token keyword">while</span> <span class="token punctuation">(</span><span class="token operator">!</span>success<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            <span class="token keyword">try</span> <span class="token punctuation">{</span>
                bluetoothSocket<span class="token punctuation">.</span><span class="token function">connect</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                success <span class="token operator">=</span> <span class="token boolean">true</span><span class="token punctuation">;</span>
            <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">IOException</span> e<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                <span class="token keyword">try</span> <span class="token punctuation">{</span>
                    bluetoothSocket<span class="token punctuation">.</span><span class="token function">close</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">IOException</span> closeException<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                    Log<span class="token punctuation">.</span><span class="token function">e</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Unable to close the socket"</span><span class="token punctuation">,</span> closeException<span class="token punctuation">)</span><span class="token punctuation">;</span>
                <span class="token punctuation">}</span>
                success <span class="token operator">=</span> <span class="token boolean">false</span><span class="token punctuation">;</span>
            <span class="token punctuation">}</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">start</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>

</code></pre>
<h5 id="bug-2-data-corruption-or-loss">Bug 2: Data Corruption or Loss</h5>
<ul>
<li><strong>Symptom:</strong> Received data on the Android device is incomplete or corrupted.</li>
<li><strong>Solution:</strong> Implement checksums or validation for received data. Ensure that data is sent in a consistent format from the Arduino.</li>
</ul>
<pre class=" language-c"><code class="prism ++ language-c"><span class="token keyword">void</span> <span class="token function">loop</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token comment">// Data transmission logic</span>
    String data <span class="token operator">=</span> <span class="token string">"{ \"time\": "</span> <span class="token operator">+</span> <span class="token function">String</span><span class="token punctuation">(</span><span class="token function">millis</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span> <span class="token operator">+</span> <span class="token string">", \"value\": "</span> <span class="token operator">+</span> <span class="token function">String</span><span class="token punctuation">(</span>encoderValue<span class="token punctuation">)</span> <span class="token operator">+</span> <span class="token string">" }"</span><span class="token punctuation">;</span>
    myBluetooth<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span>data<span class="token punctuation">)</span><span class="token punctuation">;</span>
    Serial<span class="token punctuation">.</span><span class="token function">println</span><span class="token punctuation">(</span>data<span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token function">delay</span><span class="token punctuation">(</span><span class="token number">100</span><span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">// Adjust delay to prevent data loss</span>
<span class="token punctuation">}</span>
</code></pre>
<h5 id="bug-3-application-crash-on-data-reception">Bug 3: Application Crash on Data Reception</h5>
<ul>
<li><strong>Symptom:</strong> The Android application crashes when receiving data.</li>
<li><strong>Solution:</strong> Ensure that the received data is parsed correctly and that all potential exceptions are handled.</li>
</ul>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">startReceivingData</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token keyword">new</span> <span class="token class-name">Thread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token punctuation">{</span>
        <span class="token keyword">try</span> <span class="token punctuation">{</span>
            inputStream <span class="token operator">=</span> bluetoothSocket<span class="token punctuation">.</span><span class="token function">getInputStream</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            BufferedReader reader <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">BufferedReader</span><span class="token punctuation">(</span><span class="token keyword">new</span> <span class="token class-name">InputStreamReader</span><span class="token punctuation">(</span>inputStream<span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

            <span class="token keyword">while</span> <span class="token punctuation">(</span><span class="token boolean">true</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
                <span class="token keyword">try</span> <span class="token punctuation">{</span>
                    String line <span class="token operator">=</span> reader<span class="token punctuation">.</span><span class="token function">readLine</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                    <span class="token keyword">if</span> <span class="token punctuation">(</span>line <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                        JSONObject jsonObject <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">JSONObject</span><span class="token punctuation">(</span>line<span class="token punctuation">)</span><span class="token punctuation">;</span>
                        <span class="token keyword">final</span> <span class="token keyword">double</span> time <span class="token operator">=</span> jsonObject<span class="token punctuation">.</span><span class="token function">getDouble</span><span class="token punctuation">(</span><span class="token string">"time"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                        <span class="token keyword">final</span> <span class="token keyword">double</span> value <span class="token operator">=</span> jsonObject<span class="token punctuation">.</span><span class="token function">getDouble</span><span class="token punctuation">(</span><span class="token string">"value"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

                        <span class="token function">runOnUiThread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token function">addDataPoint</span><span class="token punctuation">(</span>time<span class="token punctuation">,</span> value<span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                    <span class="token punctuation">}</span>
                <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">JSONException</span> e<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                    Log<span class="token punctuation">.</span><span class="token function">e</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"JSON parsing error"</span><span class="token punctuation">,</span> e<span class="token punctuation">)</span><span class="token punctuation">;</span>
                <span class="token punctuation">}</span>
            <span class="token punctuation">}</span>
        <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">IOException</span> e<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            Log<span class="token punctuation">.</span><span class="token function">e</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Error in receiving data"</span><span class="token punctuation">,</span> e<span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">start</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>

<span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">addDataPoint</span><span class="token punctuation">(</span><span class="token keyword">double</span> time<span class="token punctuation">,</span> <span class="token keyword">double</span> value<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    graphLastXValue <span class="token operator">+=</span> <span class="token number">1d</span><span class="token punctuation">;</span>
    series<span class="token punctuation">.</span><span class="token function">appendData</span><span class="token punctuation">(</span><span class="token keyword">new</span> <span class="token class-name">DataPoint</span><span class="token punctuation">(</span>graphLastXValue<span class="token punctuation">,</span> value<span class="token punctuation">)</span><span class="token punctuation">,</span> <span class="token boolean">true</span><span class="token punctuation">,</span> <span class="token number">100</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre>
<h5 id="bug-4-bluetooth-unable-to-connect-after-multiple-tests">Bug 4: Bluetooth Unable to Connect After Multiple Tests</h5>
<ul>
<li><strong>Symptom:</strong> After multiple tests, the Bluetooth connection fails to establish.</li>
<li><strong>Cause:</strong> The issue was traced back to the missing unregistration of broadcast receivers in the <code>onDestroy</code> method.</li>
<li><strong>Solution:</strong> Properly unregister the broadcast receivers to prevent resource leaks and ensure a clean state for each test.</li>
</ul>
<pre class=" language-java"><code class="prism  language-java"><span class="token annotation punctuation">@Override</span>
<span class="token keyword">protected</span> <span class="token keyword">void</span> <span class="token function">onDestroy</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token keyword">super</span><span class="token punctuation">.</span><span class="token function">onDestroy</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token comment">// Unregister discovery broadcast receiver</span>
    <span class="token keyword">try</span> <span class="token punctuation">{</span>
        <span class="token function">unregisterReceiver</span><span class="token punctuation">(</span>receiver<span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">IllegalArgumentException</span> e<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        Log<span class="token punctuation">.</span><span class="token function">e</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Receiver not registered"</span><span class="token punctuation">,</span> e<span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
    <span class="token comment">// Unregister Bluetooth connection state change broadcast receiver</span>
    <span class="token keyword">try</span> <span class="token punctuation">{</span>
        <span class="token function">unregisterReceiver</span><span class="token punctuation">(</span>mReceiver<span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">IllegalArgumentException</span> e<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        Log<span class="token punctuation">.</span><span class="token function">e</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Receiver not registered"</span><span class="token punctuation">,</span> e<span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre>
<h2 id="date-2024-04-24">[Date: 2024-04-24]</h2>
<h3 id="todays-tasks-6">Today’s Tasks</h3>
<ul>
<li>Task 1: Establish a BLE connection using GATT.</li>
<li>Task 2: Handle service and characteristic discovery.</li>
<li>Task 3: Manage data transmission over BLE.</li>
</ul>
<h3 id="detailed-notes-6">Detailed Notes</h3>
<h4 id="task-1-establish-a-ble-connection-using-gatt">Task 1: [Establish a BLE Connection Using GATT]</h4>
<p>The first task focused on establishing a BLE connection using the Generic Attribute Profile (GATT).</p>
<h5 id="steps-13">Steps:</h5>
<ul>
<li>Implemented GATT connection logic in the <code>ConnectThread</code>.</li>
<li>Added necessary permissions and initialization for BLE scanning and connection.</li>
</ul>
<p>Here’s a snippet of the updated <code>ConnectThread</code>:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">private</span> <span class="token keyword">class</span> <span class="token class-name">ConnectThread</span> <span class="token keyword">extends</span> <span class="token class-name">Thread</span> <span class="token punctuation">{</span>
    <span class="token keyword">private</span> <span class="token keyword">final</span> BluetoothDevice mmDevice<span class="token punctuation">;</span> <span class="token comment">// BluetoothDevice variable</span>
    <span class="token keyword">private</span> BluetoothSocket mmSocket<span class="token punctuation">;</span>
    <span class="token keyword">public</span> <span class="token function">ConnectThread</span><span class="token punctuation">(</span>BluetoothDevice device<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">this</span><span class="token punctuation">.</span>mmDevice <span class="token operator">=</span> device<span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

    <span class="token annotation punctuation">@SuppressLint</span><span class="token punctuation">(</span><span class="token string">"MissingPermission"</span><span class="token punctuation">)</span>
    <span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">run</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">if</span> <span class="token punctuation">(</span>mmDevice<span class="token punctuation">.</span><span class="token function">getType</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">==</span> BluetoothDevice<span class="token punctuation">.</span>DEVICE_TYPE_LE<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            <span class="token comment">// BLE device, use GATT to connect</span>
            bluetoothAdapter<span class="token punctuation">.</span><span class="token function">cancelDiscovery</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            bluetoothGatt <span class="token operator">=</span> mmDevice<span class="token punctuation">.</span><span class="token function">connectGatt</span><span class="token punctuation">(</span>MainActivity<span class="token punctuation">.</span><span class="token keyword">this</span><span class="token punctuation">,</span> <span class="token boolean">false</span><span class="token punctuation">,</span> <span class="token keyword">new</span> <span class="token class-name">BluetoothGattCallback</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
                <span class="token annotation punctuation">@Override</span>
                <span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">onConnectionStateChange</span><span class="token punctuation">(</span>BluetoothGatt gatt<span class="token punctuation">,</span> <span class="token keyword">int</span> status<span class="token punctuation">,</span> <span class="token keyword">int</span> newState<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                    <span class="token keyword">super</span><span class="token punctuation">.</span><span class="token function">onConnectionStateChange</span><span class="token punctuation">(</span>gatt<span class="token punctuation">,</span> status<span class="token punctuation">,</span> newState<span class="token punctuation">)</span><span class="token punctuation">;</span>
                    <span class="token keyword">if</span> <span class="token punctuation">(</span>newState <span class="token operator">==</span> BluetoothProfile<span class="token punctuation">.</span>STATE_CONNECTED<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                        Log<span class="token punctuation">.</span><span class="token function">i</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Connected to GATT server."</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                        gatt<span class="token punctuation">.</span><span class="token function">discoverServices</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">// Start service discovery</span>

                        <span class="token function">runOnUiThread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token punctuation">{</span>
                            <span class="token keyword">if</span> <span class="token punctuation">(</span>btnConnect <span class="token operator">!=</span> null <span class="token operator">&amp;&amp;</span> btDeviceTextView <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                                btnConnect<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Disconnect"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                                btDeviceTextView<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Connected device: "</span> <span class="token operator">+</span> mmDevice<span class="token punctuation">.</span><span class="token function">getName</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                                isConnected <span class="token operator">=</span> <span class="token boolean">true</span><span class="token punctuation">;</span>
                            <span class="token punctuation">}</span>
                        <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                    <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token keyword">if</span> <span class="token punctuation">(</span>newState <span class="token operator">==</span> BluetoothProfile<span class="token punctuation">.</span>STATE_DISCONNECTED<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                        Log<span class="token punctuation">.</span><span class="token function">i</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Disconnected from GATT server."</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

                        <span class="token function">runOnUiThread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token punctuation">{</span>
                            <span class="token keyword">if</span> <span class="token punctuation">(</span>btnConnect <span class="token operator">!=</span> null <span class="token operator">&amp;&amp;</span> btDeviceTextView <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                                btnConnect<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Connect"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                                btDeviceTextView<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Please select a device"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                                isConnected <span class="token operator">=</span> <span class="token boolean">false</span><span class="token punctuation">;</span>
                            <span class="token punctuation">}</span>
                        <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                    <span class="token punctuation">}</span>
                <span class="token punctuation">}</span>

                <span class="token annotation punctuation">@Override</span>
                <span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">onServicesDiscovered</span><span class="token punctuation">(</span>BluetoothGatt gatt<span class="token punctuation">,</span> <span class="token keyword">int</span> status<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                    <span class="token keyword">super</span><span class="token punctuation">.</span><span class="token function">onServicesDiscovered</span><span class="token punctuation">(</span>gatt<span class="token punctuation">,</span> status<span class="token punctuation">)</span><span class="token punctuation">;</span>
                    String message<span class="token punctuation">;</span>
                    <span class="token keyword">if</span> <span class="token punctuation">(</span>status <span class="token operator">==</span> BluetoothGatt<span class="token punctuation">.</span>GATT_SUCCESS<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                        BluetoothGattService service <span class="token operator">=</span> gatt<span class="token punctuation">.</span><span class="token function">getService</span><span class="token punctuation">(</span>SERVICE_UUID<span class="token punctuation">)</span><span class="token punctuation">;</span>
                        <span class="token keyword">if</span> <span class="token punctuation">(</span>service <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                            BluetoothGattCharacteristic characteristic <span class="token operator">=</span> service<span class="token punctuation">.</span><span class="token function">getCharacteristic</span><span class="token punctuation">(</span>CHARACTERISTIC_UUID<span class="token punctuation">)</span><span class="token punctuation">;</span>
                            <span class="token keyword">if</span> <span class="token punctuation">(</span>characteristic <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                                gatt<span class="token punctuation">.</span><span class="token function">readCharacteristic</span><span class="token punctuation">(</span>characteristic<span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">// Read the characteristic</span>
                                message <span class="token operator">=</span> <span class="token string">"Service and characteristic UUID found."</span><span class="token punctuation">;</span>
                            <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
                                message <span class="token operator">=</span> <span class="token string">"Characteristic UUID not found."</span><span class="token punctuation">;</span>
                            <span class="token punctuation">}</span>
                        <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
                            message <span class="token operator">=</span> <span class="token string">"Service UUID not found."</span><span class="token punctuation">;</span>
                        <span class="token punctuation">}</span>
                    <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
                        message <span class="token operator">=</span> <span class="token string">"onServicesDiscovered received: "</span> <span class="token operator">+</span> status<span class="token punctuation">;</span>
                    <span class="token punctuation">}</span>
                    <span class="token function">runOnUiThread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token function">showAlertDialog</span><span class="token punctuation">(</span><span class="token string">"Service Discovery"</span><span class="token punctuation">,</span> message<span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                <span class="token punctuation">}</span>

                <span class="token annotation punctuation">@Override</span>
                <span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">onCharacteristicRead</span><span class="token punctuation">(</span>BluetoothGatt gatt<span class="token punctuation">,</span> BluetoothGattCharacteristic characteristic<span class="token punctuation">,</span> <span class="token keyword">int</span> status<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                    <span class="token keyword">super</span><span class="token punctuation">.</span><span class="token function">onCharacteristicRead</span><span class="token punctuation">(</span>gatt<span class="token punctuation">,</span> characteristic<span class="token punctuation">,</span> status<span class="token punctuation">)</span><span class="token punctuation">;</span>
                    String message<span class="token punctuation">;</span>
                    <span class="token keyword">if</span> <span class="token punctuation">(</span>status <span class="token operator">==</span> BluetoothGatt<span class="token punctuation">.</span>GATT_SUCCESS<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                        String hexValue <span class="token operator">=</span> <span class="token function">bytesToHex</span><span class="token punctuation">(</span>characteristic<span class="token punctuation">.</span><span class="token function">getValue</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                        Log<span class="token punctuation">.</span><span class="token function">i</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Characteristic value read: "</span> <span class="token operator">+</span> hexValue<span class="token punctuation">)</span><span class="token punctuation">;</span>
                        message <span class="token operator">=</span> <span class="token string">"Characteristic value read: "</span> <span class="token operator">+</span> hexValue<span class="token punctuation">;</span>
                        <span class="token function">handleCharacteristicRead</span><span class="token punctuation">(</span>characteristic<span class="token punctuation">.</span><span class="token function">getValue</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                    <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
                        message <span class="token operator">=</span> <span class="token string">"Failed to read characteristic value with status: "</span> <span class="token operator">+</span> status<span class="token punctuation">;</span>
                    <span class="token punctuation">}</span>
                    <span class="token function">runOnUiThread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token function">showAlertDialog</span><span class="token punctuation">(</span><span class="token string">"Characteristic Read"</span><span class="token punctuation">,</span> message<span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                <span class="token punctuation">}</span>
            <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
            <span class="token comment">// Classic Bluetooth, handle differently</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span>

    <span class="token keyword">private</span> String <span class="token function">bytesToHex</span><span class="token punctuation">(</span><span class="token keyword">byte</span><span class="token punctuation">[</span><span class="token punctuation">]</span> bytes<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        StringBuilder sb <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">StringBuilder</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token keyword">for</span> <span class="token punctuation">(</span><span class="token keyword">byte</span> b <span class="token operator">:</span> bytes<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            sb<span class="token punctuation">.</span><span class="token function">append</span><span class="token punctuation">(</span>String<span class="token punctuation">.</span><span class="token function">format</span><span class="token punctuation">(</span><span class="token string">"%02X "</span><span class="token punctuation">,</span> b<span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span>
        <span class="token keyword">return</span> sb<span class="token punctuation">.</span><span class="token function">toString</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">trim</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

    <span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">handleCharacteristicRead</span><span class="token punctuation">(</span><span class="token keyword">byte</span><span class="token punctuation">[</span><span class="token punctuation">]</span> value<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token function">runOnUiThread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token punctuation">{</span>
            <span class="token comment">// Update UI or handle data read from characteristic</span>
        <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre>
<h4 id="task-2-handle-service-and-characteristic-discovery">Task 2: [Handle Service and Characteristic Discovery]</h4>
<p>The second task involved handling the discovery of services and characteristics over the BLE connection.</p>
<h5 id="steps-14">Steps:</h5>
<ul>
<li>Implemented callbacks for service and characteristic discovery.</li>
<li>Ensured that the necessary services and characteristics were found and accessible.</li>
</ul>
<p>Here’s a snippet showing the service and characteristic discovery:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token annotation punctuation">@Override</span>
<span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">onServicesDiscovered</span><span class="token punctuation">(</span>BluetoothGatt gatt<span class="token punctuation">,</span> <span class="token keyword">int</span> status<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token keyword">super</span><span class="token punctuation">.</span><span class="token function">onServicesDiscovered</span><span class="token punctuation">(</span>gatt<span class="token punctuation">,</span> status<span class="token punctuation">)</span><span class="token punctuation">;</span>
    String message<span class="token punctuation">;</span>
    <span class="token keyword">if</span> <span class="token punctuation">(</span>status <span class="token operator">==</span> BluetoothGatt<span class="token punctuation">.</span>GATT_SUCCESS<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        BluetoothGattService service <span class="token operator">=</span> gatt<span class="token punctuation">.</span><span class="token function">getService</span><span class="token punctuation">(</span>SERVICE_UUID<span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token keyword">if</span> <span class="token punctuation">(</span>service <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            BluetoothGattCharacteristic characteristic <span class="token operator">=</span> service<span class="token punctuation">.</span><span class="token function">getCharacteristic</span><span class="token punctuation">(</span>CHARACTERISTIC_UUID<span class="token punctuation">)</span><span class="token punctuation">;</span>
            <span class="token keyword">if</span> <span class="token punctuation">(</span>characteristic <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                gatt<span class="token punctuation">.</span><span class="token function">readCharacteristic</span><span class="token punctuation">(</span>characteristic<span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">// Read the characteristic</span>
                message <span class="token operator">=</span> <span class="token string">"Service and characteristic UUID found."</span><span class="token punctuation">;</span>
            <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
                message <span class="token operator">=</span> <span class="token string">"Characteristic UUID not found."</span><span class="token punctuation">;</span>
            <span class="token punctuation">}</span>
        <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
            message <span class="token operator">=</span> <span class="token string">"Service UUID not found."</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
        message <span class="token operator">=</span> <span class="token string">"onServicesDiscovered received: "</span> <span class="token operator">+</span> status<span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
    <span class="token function">runOnUiThread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token function">showAlertDialog</span><span class="token punctuation">(</span><span class="token string">"Service Discovery"</span><span class="token punctuation">,</span> message<span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre>
<h4 id="task-3-manage-data-transmission-over-ble">Task 3: [Manage Data Transmission Over BLE]</h4>
<p>The third task was to manage data transmission over BLE, ensuring that data from the Arduino was transmitted efficiently and received accurately on the Android device.</p>
<h5 id="steps-15">Steps:</h5>
<ul>
<li>Implemented methods to read characteristics and handle the received data.</li>
<li>Ensured that data was transmitted and received in a consistent format.</li>
</ul>
<p>Here’s a snippet showing data transmission management:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token annotation punctuation">@Override</span>
<span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">onCharacteristicRead</span><span class="token punctuation">(</span>BluetoothGatt gatt<span class="token punctuation">,</span> BluetoothGattCharacteristic characteristic<span class="token punctuation">,</span> <span class="token keyword">int</span> status<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token keyword">super</span><span class="token punctuation">.</span><span class="token function">onCharacteristicRead</span><span class="token punctuation">(</span>gatt<span class="token punctuation">,</span> characteristic<span class="token punctuation">,</span> status<span class="token punctuation">)</span><span class="token punctuation">;</span>
    String message<span class="token punctuation">;</span>
    <span class="token keyword">if</span> <span class="token punctuation">(</span>status <span class="token operator">==</span> BluetoothGatt<span class="token punctuation">.</span>GATT_SUCCESS<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        String hexValue <span class="token operator">=</span> <span class="token function">bytesToHex</span><span class="token punctuation">(</span>characteristic<span class="token punctuation">.</span><span class="token function">getValue</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        Log<span class="token punctuation">.</span><span class="token function">i</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Characteristic value read: "</span> <span class="token operator">+</span> hexValue<span class="token punctuation">)</span><span class="token punctuation">;</span>
        message <span class="token operator">=</span> <span class="token string">"Characteristic value read: "</span> <span class="token operator">+</span> hexValue<span class="token punctuation">;</span>
        <span class="token function">handleCharacteristicRead</span><span class="token punctuation">(</span>characteristic<span class="token punctuation">.</span><span class="token function">getValue</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
        message <span class="token operator">=</span> <span class="token string">"Failed to read characteristic value with status: "</span> <span class="token operator">+</span> status<span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
    <span class="token function">runOnUiThread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token function">showAlertDialog</span><span class="token punctuation">(</span><span class="token string">"Characteristic Read"</span><span class="token punctuation">,</span> message<span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>

<span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">handleCharacteristicRead</span><span class="token punctuation">(</span><span class="token keyword">byte</span><span class="token punctuation">[</span><span class="token punctuation">]</span> value<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token function">runOnUiThread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token punctuation">{</span>
        <span class="token comment">// Update UI or handle data read from characteristic</span>
    <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre>
<h3 id="summary-4">Summary</h3>
<p>Today’s tasks focused on migrating from traditional Bluetooth to BLE, establishing a BLE connection using GATT, handling service and characteristic discovery, and managing data transmission over BLE. The transition to BLE improved efficiency and compatibility with modern devices. By implementing the necessary callbacks and handling data transmission effectively, the system now supports reliable real-time data communication over BLE.</p>
<h2 id="date-2024-04-29">[Date: 2024-04-29]</h2>
<h3 id="todays-tasks-7">Today’s Tasks</h3>
<ul>
<li>Task 1: Set up the Nordic Semiconductor nRF52840 dongle.</li>
<li>Task 2: Install and configure the Nordic SoftDevice.</li>
<li>Task 3: Manage the portico of the nRF52840 dongle.</li>
<li>Task 4: Configure UART over BLE for serial communication.</li>
</ul>
<h3 id="detailed-notes-7">Detailed Notes</h3>
<h4 id="task-1-set-up-the-nordic-semiconductor-nrf52840-dongle">Task 1: [Set Up the Nordic Semiconductor nRF52840 Dongle]</h4>
<p>Today’s primary task involved setting up the nRF52840 dongle to enhance BLE capabilities. This included using nRFgo Studio and nRF Connect for Desktop Programmer to configure the dongle.</p>
<h5 id="steps-16">Steps:</h5>
<ol>
<li>
<p><strong>Install nRFgo Studio and nRF Connect for Desktop Programmer:</strong></p>
<ul>
<li>Download and install the latest version of nRFgo Studio from the Nordic Semiconductor website.</li>
<li>Install nRF Connect for Desktop and its Programmer app from the Nordic Semiconductor website.</li>
</ul>
</li>
<li>
<p><strong>Connect the nRF52840 Dongle:</strong></p>
<ul>
<li>Connect the nRF52840 dongle to the computer via USB.</li>
<li>Open nRFgo Studio and ensure the device is recognized under the Device Manager.</li>
</ul>
</li>
</ol>
<h4 id="task-2-install-and-configure-the-nordic-softdevice">Task 2: [Install and Configure the Nordic SoftDevice]</h4>
<p>The second task focused on installing and configuring the Nordic SoftDevice using nRFgo Studio and nRF Connect for Desktop Programmer.</p>
<h5 id="steps-17">Steps:</h5>
<ol>
<li>
<p><strong>Download the SoftDevice:</strong></p>
<ul>
<li>Download the appropriate SoftDevice binary (e.g., S140) from the Nordic Semiconductor website.</li>
</ul>
</li>
<li>
<p><strong>Program the SoftDevice Using nRFgo Studio:</strong></p>
<ul>
<li>Open nRFgo Studio.</li>
<li>Select the nRF52840 dongle in the Device Manager.</li>
<li>Click on the “Program SoftDevice” tab.</li>
<li>Browse to the downloaded SoftDevice .hex file and click “Program” to flash the SoftDevice onto the dongle.</li>
</ul>
</li>
<li>
<p><strong>Using nRF Connect for Desktop Programmer:</strong></p>
<ul>
<li>Open nRF Connect for Desktop and launch the Programmer app.</li>
<li>Select the nRF52840 dongle.</li>
<li>Click “Add HEX file” and select the SoftDevice .hex file.</li>
<li>Click “Write” to flash the SoftDevice onto the dongle.</li>
</ul>
</li>
</ol>
<h4 id="task-3-manage-the-portico-of-the-nrf52840-dongle">Task 3: [Manage the Portico of the nRF52840 Dongle]</h4>
<p>The third task involved managing the portico (gateway) of the nRF52840 dongle to ensure smooth communication.</p>
<h5 id="steps-18">Steps:</h5>
<ul>
<li><strong>Configure BLE Settings:</strong>
<ul>
<li>Use the Nordic SDK to configure BLE settings and ensure proper initialization.</li>
<li>Verify the communication stability through test transmissions.</li>
</ul>
</li>
</ul>
<h4 id="task-4-configure-uart-over-ble-for-serial-communication">Task 4: [Configure UART Over BLE for Serial Communication]</h4>
<p>The fourth task was to configure UART over BLE to enable serial communication, allowing data to be sent and received wirelessly via the dongle.</p>
<h5 id="steps-19">Steps:</h5>
<ol>
<li>
<p><strong>Setup UART Service:</strong></p>
<ul>
<li>Initialize UART service using the Nordic SDK.</li>
<li>Configure the service to handle data transmissions.</li>
</ul>
</li>
</ol>
<h3 id="summary-5">Summary</h3>
<p>Today’s tasks focused on configuring the nRF52840 dongle to enhance BLE capabilities, installing and configuring the Nordic SoftDevice, managing the dongle’s portico for smooth communication, and setting up UART over BLE for serial communication. This setup improved efficiency and compatibility with modern devices, ensuring reliable wireless data transmission and reception.</p>
<p>For detailed steps on setting up and programming the SoftDevice, you can refer to the <a href="https://infocenter.nordicsemi.com">Nordic Semiconductor InfoCenter</a> and the <a href="https://devzone.nordicsemi.com">Nordic DevZone</a>​ (<a href="https://developer.nordicsemi.com/nRF5_SDK/nRF51_SDK_v8.x.x/doc/8.1.0/s130/html/a00018.html">Nordic Semiconductor Developer Hub</a>)​​ (<a href="https://infocenter.nordicsemi.com/topic/com.nordic.infocenter.sdk51.v10.0.0/getting_started_softdevice.html">Nordic Semi Info Center</a>)​​ (<a href="https://devzone.nordicsemi.com/nordic/nordic-blog/b/blog/posts/step-by-step-guide-to-setup-and-start-developemt-w">Nordic DevZone</a>)​​ (<a href="https://devzone.nordicsemi.com/guides/short-range-guides/b/software-development-kit/posts/getting-started-with-nordics-secure-dfu-bootloader">Nordic DevZone</a>)​​ (<a href="https://infocenter.nordicsemi.com/topic/sdk_nrf5_v17.0.2/getting_started_examples.html">Nordic Semi Info Center</a>)​.</p>
<h2 id="date-2024-05-16">[Date: 2024-05-16]</h2>
<h3 id="todays-tasks-8">Today’s Tasks</h3>
<ul>
<li>Task 1: Establish a BLE connection using GATT.</li>
<li>Task 2: Configure service and characteristic discovery for UART communication.</li>
<li>Task 3: Handle data received from the RX characteristic and dynamically update the user interface.</li>
</ul>
<h3 id="detailed-notes-8">Detailed Notes</h3>
<h4 id="task-1-establish-a-ble-connection-using-gatt-1">Task 1: [Establish a BLE Connection Using GATT]</h4>
<p>The first task focused on enabling real-time communication with BLE devices by establishing a BLE connection using the Generic Attribute Profile (GATT).</p>
<h5 id="steps-20">Steps:</h5>
<ul>
<li>Implemented GATT connection logic in the <code>ConnectThread</code>.</li>
<li>Added necessary permissions and initialization for BLE scanning and connection.</li>
</ul>
<p>Here’s a snippet of the updated <code>ConnectThread</code>:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">private</span> <span class="token keyword">class</span> <span class="token class-name">ConnectThread</span> <span class="token keyword">extends</span> <span class="token class-name">Thread</span> <span class="token punctuation">{</span>
    <span class="token keyword">private</span> <span class="token keyword">final</span> BluetoothDevice mmDevice<span class="token punctuation">;</span> <span class="token comment">// BluetoothDevice variable</span>

    <span class="token keyword">public</span> <span class="token function">ConnectThread</span><span class="token punctuation">(</span>BluetoothDevice device<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">this</span><span class="token punctuation">.</span>mmDevice <span class="token operator">=</span> device<span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

    <span class="token annotation punctuation">@SuppressLint</span><span class="token punctuation">(</span><span class="token string">"MissingPermission"</span><span class="token punctuation">)</span>
    <span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">run</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">if</span> <span class="token punctuation">(</span>mmDevice<span class="token punctuation">.</span><span class="token function">getType</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">==</span> BluetoothDevice<span class="token punctuation">.</span>DEVICE_TYPE_LE<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            <span class="token comment">// BLE device, use GATT to connect</span>
            bluetoothAdapter<span class="token punctuation">.</span><span class="token function">cancelDiscovery</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            bluetoothGatt <span class="token operator">=</span> mmDevice<span class="token punctuation">.</span><span class="token function">connectGatt</span><span class="token punctuation">(</span>MainActivity<span class="token punctuation">.</span><span class="token keyword">this</span><span class="token punctuation">,</span> <span class="token boolean">false</span><span class="token punctuation">,</span> <span class="token keyword">new</span> <span class="token class-name">BluetoothGattCallback</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
                <span class="token annotation punctuation">@Override</span>
                <span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">onConnectionStateChange</span><span class="token punctuation">(</span>BluetoothGatt gatt<span class="token punctuation">,</span> <span class="token keyword">int</span> status<span class="token punctuation">,</span> <span class="token keyword">int</span> newState<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                    <span class="token keyword">super</span><span class="token punctuation">.</span><span class="token function">onConnectionStateChange</span><span class="token punctuation">(</span>gatt<span class="token punctuation">,</span> status<span class="token punctuation">,</span> newState<span class="token punctuation">)</span><span class="token punctuation">;</span>
                    <span class="token keyword">if</span> <span class="token punctuation">(</span>newState <span class="token operator">==</span> BluetoothProfile<span class="token punctuation">.</span>STATE_CONNECTED<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                        Log<span class="token punctuation">.</span><span class="token function">i</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Connected to GATT server."</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                        gatt<span class="token punctuation">.</span><span class="token function">discoverServices</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span> <span class="token comment">// Start service discovery</span>

                        <span class="token function">runOnUiThread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token punctuation">{</span>
                            <span class="token keyword">if</span> <span class="token punctuation">(</span>btnConnect <span class="token operator">!=</span> null <span class="token operator">&amp;&amp;</span> btDeviceTextView <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                                btnConnect<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Disconnect"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                                btDeviceTextView<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Connected device: "</span> <span class="token operator">+</span> mmDevice<span class="token punctuation">.</span><span class="token function">getName</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                                isConnected <span class="token operator">=</span> <span class="token boolean">true</span><span class="token punctuation">;</span>
                            <span class="token punctuation">}</span>
                        <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                    <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token keyword">if</span> <span class="token punctuation">(</span>newState <span class="token operator">==</span> BluetoothProfile<span class="token punctuation">.</span>STATE_DISCONNECTED<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                        Log<span class="token punctuation">.</span><span class="token function">i</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Disconnected from GATT server."</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

                        <span class="token function">runOnUiThread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token punctuation">{</span>
                            <span class="token keyword">if</span> <span class="token punctuation">(</span>btnConnect <span class="token operator">!=</span> null <span class="token operator">&amp;&amp;</span> btDeviceTextView <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                                btnConnect<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Connect"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                                btDeviceTextView<span class="token punctuation">.</span><span class="token function">setText</span><span class="token punctuation">(</span><span class="token string">"Please select a device"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                                isConnected <span class="token operator">=</span> <span class="token boolean">false</span><span class="token punctuation">;</span>
                            <span class="token punctuation">}</span>
                        <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                    <span class="token punctuation">}</span>
                <span class="token punctuation">}</span>
            <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
            <span class="token comment">// Classic Bluetooth, handle differently</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre>
<h4 id="task-2-configure-service-and-characteristic-discovery-for-uart-communication">Task 2: [Configure Service and Characteristic Discovery for UART Communication]</h4>
<p>The second task involved configuring the discovery of services and characteristics for UART communication over the BLE connection.</p>
<h5 id="steps-21">Steps:</h5>
<ul>
<li>Implemented callbacks for service and characteristic discovery.</li>
<li>Enabled notifications for the TX characteristic.</li>
</ul>
<p>Here’s a snippet showing the service and characteristic discovery:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token annotation punctuation">@Override</span>
<span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">onServicesDiscovered</span><span class="token punctuation">(</span>BluetoothGatt gatt<span class="token punctuation">,</span> <span class="token keyword">int</span> status<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token keyword">super</span><span class="token punctuation">.</span><span class="token function">onServicesDiscovered</span><span class="token punctuation">(</span>gatt<span class="token punctuation">,</span> status<span class="token punctuation">)</span><span class="token punctuation">;</span>
    String message<span class="token punctuation">;</span>
    <span class="token keyword">if</span> <span class="token punctuation">(</span>status <span class="token operator">==</span> BluetoothGatt<span class="token punctuation">.</span>GATT_SUCCESS<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        BluetoothGattService service <span class="token operator">=</span> gatt<span class="token punctuation">.</span><span class="token function">getService</span><span class="token punctuation">(</span>SERVICE_UUID<span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token keyword">if</span> <span class="token punctuation">(</span>service <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            BluetoothGattCharacteristic txCharacteristic <span class="token operator">=</span> service<span class="token punctuation">.</span><span class="token function">getCharacteristic</span><span class="token punctuation">(</span>TX_CHARACTERISTIC_UUID<span class="token punctuation">)</span><span class="token punctuation">;</span>
            BluetoothGattCharacteristic rxCharacteristic <span class="token operator">=</span> service<span class="token punctuation">.</span><span class="token function">getCharacteristic</span><span class="token punctuation">(</span>RX_CHARACTERISTIC_UUID<span class="token punctuation">)</span><span class="token punctuation">;</span>
            <span class="token keyword">if</span> <span class="token punctuation">(</span>txCharacteristic <span class="token operator">!=</span> null <span class="token operator">&amp;&amp;</span> rxCharacteristic <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                <span class="token comment">// Enable notifications for TX characteristic</span>
                gatt<span class="token punctuation">.</span><span class="token function">setCharacteristicNotification</span><span class="token punctuation">(</span>txCharacteristic<span class="token punctuation">,</span> <span class="token boolean">true</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                BluetoothGattDescriptor descriptor <span class="token operator">=</span> txCharacteristic<span class="token punctuation">.</span><span class="token function">getDescriptor</span><span class="token punctuation">(</span>UUID<span class="token punctuation">.</span><span class="token function">fromString</span><span class="token punctuation">(</span><span class="token string">"00002902-0000-1000-8000-00805f9b34fb"</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
                <span class="token keyword">if</span> <span class="token punctuation">(</span>descriptor <span class="token operator">!=</span> null<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                    descriptor<span class="token punctuation">.</span><span class="token function">setValue</span><span class="token punctuation">(</span>BluetoothGattDescriptor<span class="token punctuation">.</span>ENABLE_NOTIFICATION_VALUE<span class="token punctuation">)</span><span class="token punctuation">;</span>
                    gatt<span class="token punctuation">.</span><span class="token function">writeDescriptor</span><span class="token punctuation">(</span>descriptor<span class="token punctuation">)</span><span class="token punctuation">;</span>
                <span class="token punctuation">}</span>
                message <span class="token operator">=</span> <span class="token string">"Service and characteristics UUID found and notification enabled."</span><span class="token punctuation">;</span>
            <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
                message <span class="token operator">=</span> <span class="token string">"Characteristics UUID not found."</span><span class="token punctuation">;</span>
            <span class="token punctuation">}</span>
        <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
            message <span class="token operator">=</span> <span class="token string">"Service UUID not found."</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span> <span class="token keyword">else</span> <span class="token punctuation">{</span>
        message <span class="token operator">=</span> <span class="token string">"onServicesDiscovered received: "</span> <span class="token operator">+</span> status<span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
    Log<span class="token punctuation">.</span><span class="token function">i</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> message<span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token function">runOnUiThread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token function">showAlertDialog</span><span class="token punctuation">(</span><span class="token string">"Service Discovery"</span><span class="token punctuation">,</span> message<span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre>
<h4 id="task-3-handle-data-received-from-the-rx-characteristic-and-dynamically-update-the-user-interface">Task 3: [Handle Data Received from the RX Characteristic and Dynamically Update the User Interface]</h4>
<p>The third task was to handle data received from the RX characteristic, parse it, and dynamically update the user interface.</p>
<h5 id="steps-22">Steps:</h5>
<ul>
<li>Implemented methods to handle characteristic changes and parse the received data.</li>
<li>Updated the user interface dynamically based on the received data.</li>
</ul>
<p>Here’s a snippet showing the data handling:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token annotation punctuation">@Override</span>
<span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">onCharacteristicChanged</span><span class="token punctuation">(</span>BluetoothGatt gatt<span class="token punctuation">,</span> BluetoothGattCharacteristic characteristic<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token keyword">super</span><span class="token punctuation">.</span><span class="token function">onCharacteristicChanged</span><span class="token punctuation">(</span>gatt<span class="token punctuation">,</span> characteristic<span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token keyword">byte</span><span class="token punctuation">[</span><span class="token punctuation">]</span> value <span class="token operator">=</span> characteristic<span class="token punctuation">.</span><span class="token function">getValue</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    Log<span class="token punctuation">.</span><span class="token function">d</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Received data: "</span> <span class="token operator">+</span> <span class="token function">bytesToHex</span><span class="token punctuation">(</span>value<span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    dataQueue<span class="token punctuation">.</span><span class="token function">add</span><span class="token punctuation">(</span>characteristic<span class="token punctuation">.</span><span class="token function">getValue</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>

<span class="token keyword">private</span> String <span class="token function">bytesToHex</span><span class="token punctuation">(</span><span class="token keyword">byte</span><span class="token punctuation">[</span><span class="token punctuation">]</span> bytes<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    StringBuilder sb <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">StringBuilder</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token keyword">for</span> <span class="token punctuation">(</span><span class="token keyword">byte</span> b <span class="token operator">:</span> bytes<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        sb<span class="token punctuation">.</span><span class="token function">append</span><span class="token punctuation">(</span>String<span class="token punctuation">.</span><span class="token function">format</span><span class="token punctuation">(</span><span class="token string">"%02X "</span><span class="token punctuation">,</span> b<span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
    <span class="token keyword">return</span> sb<span class="token punctuation">.</span><span class="token function">toString</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">trim</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>

<span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">handleCharacteristicRead</span><span class="token punctuation">(</span><span class="token keyword">byte</span><span class="token punctuation">[</span><span class="token punctuation">]</span> value<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token function">runOnUiThread</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">-</span><span class="token operator">&gt;</span> <span class="token punctuation">{</span>
        <span class="token comment">// Update UI or handle data read from characteristic</span>
        <span class="token comment">// Example: Parse the data and update the graph</span>
        <span class="token keyword">try</span> <span class="token punctuation">{</span>
            String receivedData <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">String</span><span class="token punctuation">(</span>value<span class="token punctuation">)</span><span class="token punctuation">;</span>
            JSONObject json <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">JSONObject</span><span class="token punctuation">(</span>receivedData<span class="token punctuation">)</span><span class="token punctuation">;</span>
            SineWaveData data <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">SineWaveData</span><span class="token punctuation">(</span>json<span class="token punctuation">.</span><span class="token function">getDouble</span><span class="token punctuation">(</span><span class="token string">"time"</span><span class="token punctuation">)</span><span class="token punctuation">,</span> json<span class="token punctuation">.</span><span class="token function">getDouble</span><span class="token punctuation">(</span><span class="token string">"value"</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
            hasNewData <span class="token operator">=</span> <span class="token boolean">true</span><span class="token punctuation">;</span>
            lastData <span class="token operator">=</span> data<span class="token punctuation">;</span>
            <span class="token function">updateGraph</span><span class="token punctuation">(</span>data<span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span> <span class="token keyword">catch</span> <span class="token punctuation">(</span><span class="token class-name">JSONException</span> e<span class="token punctuation">)</span> <span class="token punctuation">{</span>
            Log<span class="token punctuation">.</span><span class="token function">e</span><span class="token punctuation">(</span>TAG<span class="token punctuation">,</span> <span class="token string">"Failed to parse data"</span><span class="token punctuation">,</span> e<span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span>
    <span class="token punctuation">}</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre>
<h3 id="summary-6">Summary</h3>
<p>Today’s tasks focused on enabling real-time communication with BLE devices on the mobile application. By establishing a BLE connection using GATT, configuring service and characteristic discovery for UART communication, and enabling notifications for the TX characteristic, the application can now handle data received from the RX characteristic and dynamically update the user interface.</p>
<h2 id="date-2024-05-19">[Date: 2024-05-19]</h2>
<h3 id="todays-tasks-9">Today’s Tasks</h3>
<ul>
<li>Task 1: Parse and handle received information.</li>
<li>Task 2: Visualize tangent force, efficiency, and push arc based on received data.</li>
</ul>
<h3 id="detailed-notes-9">Detailed Notes</h3>
<h4 id="task-1-parse-and-handle-received-information">Task 1: [Parse and Handle Received Information]</h4>
<p>The first task involved parsing and handling the data received over the BLE connection.</p>
<h5 id="steps-23">Steps:</h5>
<ul>
<li>Implemented callbacks for service and characteristic discovery.</li>
<li>Parsed data received from the RX characteristic to extract relevant values.</li>
</ul>
<p>Here’s a snippet showing the data parsing:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">private</span> <span class="token keyword">double</span><span class="token punctuation">[</span><span class="token punctuation">]</span> <span class="token function">parseData</span><span class="token punctuation">(</span><span class="token keyword">byte</span><span class="token punctuation">[</span><span class="token punctuation">]</span> value<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token comment">// Convert bytes to a list of doubles</span>
    <span class="token keyword">double</span><span class="token punctuation">[</span><span class="token punctuation">]</span> data <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">double</span><span class="token punctuation">[</span><span class="token number">10</span><span class="token punctuation">]</span><span class="token punctuation">;</span>
    <span class="token keyword">for</span> <span class="token punctuation">(</span><span class="token keyword">int</span> i <span class="token operator">=</span> <span class="token number">0</span><span class="token punctuation">;</span> i <span class="token operator">&lt;</span> <span class="token number">10</span><span class="token punctuation">;</span> i<span class="token operator">++</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">long</span> bits <span class="token operator">=</span> <span class="token number">0</span><span class="token punctuation">;</span>
        <span class="token keyword">for</span> <span class="token punctuation">(</span><span class="token keyword">int</span> j <span class="token operator">=</span> <span class="token number">0</span><span class="token punctuation">;</span> j <span class="token operator">&lt;</span> <span class="token number">8</span><span class="token punctuation">;</span> j<span class="token operator">++</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
            bits <span class="token operator">|=</span> <span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token keyword">long</span><span class="token punctuation">)</span> value<span class="token punctuation">[</span>i <span class="token operator">*</span> <span class="token number">8</span> <span class="token operator">+</span> j<span class="token punctuation">]</span> <span class="token operator">&amp;</span> <span class="token number">0xFF</span><span class="token punctuation">)</span> <span class="token operator">&lt;&lt;</span> <span class="token punctuation">(</span><span class="token number">8</span> <span class="token operator">*</span> <span class="token punctuation">(</span><span class="token number">7</span> <span class="token operator">-</span> j<span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token punctuation">}</span>
        data<span class="token punctuation">[</span>i<span class="token punctuation">]</span> <span class="token operator">=</span> Double<span class="token punctuation">.</span><span class="token function">longBitsToDouble</span><span class="token punctuation">(</span>bits<span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
    <span class="token keyword">return</span> data<span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre>
<h4 id="task-2-visualize-tangent-force-efficiency-and-push-arc-based-on-received-data">Task 2: [Visualize Tangent Force, Efficiency, and Push Arc Based on Received Data]</h4>
<p>The second task was to visualize the tangent force, efficiency, and push arc metrics based on the parsed data, updating the user interface dynamically.</p>
<h5 id="steps-24">Steps:</h5>
<ul>
<li>Implemented methods to handle characteristic changes and parse the received data.</li>
<li>Utilized custom views to plot the extracted data in real-time.</li>
</ul>
<p>Here’s a snippet showing the data visualization:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">updateViews</span><span class="token punctuation">(</span><span class="token keyword">double</span><span class="token punctuation">[</span><span class="token punctuation">]</span> data<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    TangentForceView tangentForceView <span class="token operator">=</span> <span class="token function">findViewById</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>id<span class="token punctuation">.</span>tangentForceView<span class="token punctuation">)</span><span class="token punctuation">;</span>
    EfficiencyView efficiencyView <span class="token operator">=</span> <span class="token function">findViewById</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>id<span class="token punctuation">.</span>efficiencyView<span class="token punctuation">)</span><span class="token punctuation">;</span>
    PushArcView pushArcView <span class="token operator">=</span> <span class="token function">findViewById</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>id<span class="token punctuation">.</span>pushArcView<span class="token punctuation">)</span><span class="token punctuation">;</span>

    tangentForceView<span class="token punctuation">.</span><span class="token function">addData</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token keyword">float</span><span class="token punctuation">)</span> data<span class="token punctuation">[</span><span class="token number">1</span><span class="token punctuation">]</span><span class="token punctuation">,</span> <span class="token punctuation">(</span><span class="token keyword">float</span><span class="token punctuation">)</span> data<span class="token punctuation">[</span><span class="token number">6</span><span class="token punctuation">]</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    efficiencyView<span class="token punctuation">.</span><span class="token function">addData</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token keyword">float</span><span class="token punctuation">)</span> data<span class="token punctuation">[</span><span class="token number">4</span><span class="token punctuation">]</span><span class="token punctuation">,</span> <span class="token punctuation">(</span><span class="token keyword">float</span><span class="token punctuation">)</span> data<span class="token punctuation">[</span><span class="token number">9</span><span class="token punctuation">]</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    pushArcView<span class="token punctuation">.</span><span class="token function">addData</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token keyword">float</span><span class="token punctuation">)</span> data<span class="token punctuation">[</span><span class="token number">2</span><span class="token punctuation">]</span><span class="token punctuation">,</span> <span class="token punctuation">(</span><span class="token keyword">float</span><span class="token punctuation">)</span> data<span class="token punctuation">[</span><span class="token number">7</span><span class="token punctuation">]</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre>
<h3 id="summary-7">Summary</h3>
<p>Today’s tasks focused on extending the BLE communication functionality by parsing and handling received data, and visualizing key metrics in real-time. By parsing data received over BLE and utilizing custom views to display tangent force, efficiency, and push arc, the application now provides a dynamic and interactive user experience.</p>
<h2 id="date-2024-05-23">[Date: 2024-05-23]</h2>
<h3 id="todays-tasks-10">Today’s Tasks</h3>
<ul>
<li>Task 1: Integrate AndroidPlot library for improved rendering performance.</li>
<li>Task 2: Configure plots for tangent force, efficiency, and push arc.</li>
<li>Task 3: Implement dynamic updates and refine UI for real-time data visualization.</li>
</ul>
<h3 id="detailed-notes-10">Detailed Notes</h3>
<h4 id="task-1-integrate-androidplot-library-for-improved-rendering-performance">Task 1: [Integrate AndroidPlot Library for Improved Rendering Performance]</h4>
<p>The first task focused on integrating the AndroidPlot library to enhance the rendering performance and overall appearance of the user interface.</p>
<h5 id="steps-25">Steps:</h5>
<ul>
<li>Added the AndroidPlot library to the project.</li>
<li>Set up the library for use in the application.</li>
</ul>
<p>Here’s a snippet showing the library setup:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token comment">// Add dependencies in build.gradle</span>
dependencies <span class="token punctuation">{</span>
    implementation <span class="token string">'com.androidplot:androidplot-core:1.5.7'</span>
<span class="token punctuation">}</span>
</code></pre>
<h4 id="task-2-configure-plots-for-tangent-force-efficiency-and-push-arc">Task 2: [Configure Plots for Tangent Force, Efficiency, and Push Arc]</h4>
<p>The second task involved configuring the plots for tangent force, efficiency, and push arc using the AndroidPlot library.</p>
<h5 id="steps-26">Steps:</h5>
<ul>
<li>Created and configured plots using <code>SimpleXYSeries</code> and <code>LineAndPointFormatter</code>.</li>
<li>Set up plots to ensure smooth and accurate data rendering.</li>
</ul>
<p>Here’s a snippet showing plot configuration:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token comment">// Initialize the plot for tangent force</span>
XYPlot tangentForcePlot <span class="token operator">=</span> <span class="token function">findViewById</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>id<span class="token punctuation">.</span>tangentForcePlot<span class="token punctuation">)</span><span class="token punctuation">;</span>
SimpleXYSeries tangentForceSeries <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">SimpleXYSeries</span><span class="token punctuation">(</span><span class="token string">"Tangent Force"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
LineAndPointFormatter tangentForceFormatter <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">LineAndPointFormatter</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
tangentForcePlot<span class="token punctuation">.</span><span class="token function">addSeries</span><span class="token punctuation">(</span>tangentForceSeries<span class="token punctuation">,</span> tangentForceFormatter<span class="token punctuation">)</span><span class="token punctuation">;</span>

<span class="token comment">// Initialize the plot for efficiency</span>
XYPlot efficiencyPlot <span class="token operator">=</span> <span class="token function">findViewById</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>id<span class="token punctuation">.</span>efficiencyPlot<span class="token punctuation">)</span><span class="token punctuation">;</span>
SimpleXYSeries efficiencySeries <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">SimpleXYSeries</span><span class="token punctuation">(</span><span class="token string">"Efficiency"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
LineAndPointFormatter efficiencyFormatter <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">LineAndPointFormatter</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
efficiencyPlot<span class="token punctuation">.</span><span class="token function">addSeries</span><span class="token punctuation">(</span>efficiencySeries<span class="token punctuation">,</span> efficiencyFormatter<span class="token punctuation">)</span><span class="token punctuation">;</span>

<span class="token comment">// Initialize the plot for push arc</span>
XYPlot pushArcPlot <span class="token operator">=</span> <span class="token function">findViewById</span><span class="token punctuation">(</span>R<span class="token punctuation">.</span>id<span class="token punctuation">.</span>pushArcPlot<span class="token punctuation">)</span><span class="token punctuation">;</span>
SimpleXYSeries pushArcSeries <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">SimpleXYSeries</span><span class="token punctuation">(</span><span class="token string">"Push Arc"</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
LineAndPointFormatter pushArcFormatter <span class="token operator">=</span> <span class="token keyword">new</span> <span class="token class-name">LineAndPointFormatter</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
pushArcPlot<span class="token punctuation">.</span><span class="token function">addSeries</span><span class="token punctuation">(</span>pushArcSeries<span class="token punctuation">,</span> pushArcFormatter<span class="token punctuation">)</span><span class="token punctuation">;</span>
</code></pre>
<h4 id="task-3-implement-dynamic-updates-and-refine-ui-for-real-time-data-visualization">Task 3: [Implement Dynamic Updates and Refine UI for Real-time Data Visualization]</h4>
<p>The third task was to implement dynamic updates to the plots to ensure real-time data visualization and refine the UI to be more professional and user-friendly.</p>
<h5 id="steps-27">Steps:</h5>
<ul>
<li>Implemented dynamic updates to the plots using data received over BLE.</li>
<li>Refined the UI to provide clear, accurate, and aesthetically pleasing data visualizations.</li>
</ul>
<p>Here’s a snippet showing dynamic updates:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token keyword">private</span> <span class="token keyword">void</span> <span class="token function">updateViews</span><span class="token punctuation">(</span><span class="token keyword">double</span><span class="token punctuation">[</span><span class="token punctuation">]</span> data<span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token comment">// Update tangent force plot</span>
    tangentForceSeries<span class="token punctuation">.</span><span class="token function">setModel</span><span class="token punctuation">(</span>Arrays<span class="token punctuation">.</span><span class="token function">asList</span><span class="token punctuation">(</span>data<span class="token punctuation">[</span><span class="token number">1</span><span class="token punctuation">]</span><span class="token punctuation">,</span> data<span class="token punctuation">[</span><span class="token number">6</span><span class="token punctuation">]</span><span class="token punctuation">)</span><span class="token punctuation">,</span> SimpleXYSeries<span class="token punctuation">.</span>ArrayFormat<span class="token punctuation">.</span>Y_VALS_ONLY<span class="token punctuation">)</span><span class="token punctuation">;</span>
    tangentForcePlot<span class="token punctuation">.</span><span class="token function">redraw</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token comment">// Update efficiency plot</span>
    efficiencySeries<span class="token punctuation">.</span><span class="token function">setModel</span><span class="token punctuation">(</span>Arrays<span class="token punctuation">.</span><span class="token function">asList</span><span class="token punctuation">(</span>data<span class="token punctuation">[</span><span class="token number">4</span><span class="token punctuation">]</span><span class="token punctuation">,</span> data<span class="token punctuation">[</span><span class="token number">9</span><span class="token punctuation">]</span><span class="token punctuation">)</span><span class="token punctuation">,</span> SimpleXYSeries<span class="token punctuation">.</span>ArrayFormat<span class="token punctuation">.</span>Y_VALS_ONLY<span class="token punctuation">)</span><span class="token punctuation">;</span>
    efficiencyPlot<span class="token punctuation">.</span><span class="token function">redraw</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token comment">// Update push arc plot</span>
    pushArcSeries<span class="token punctuation">.</span><span class="token function">setModel</span><span class="token punctuation">(</span>Arrays<span class="token punctuation">.</span><span class="token function">asList</span><span class="token punctuation">(</span>data<span class="token punctuation">[</span><span class="token number">2</span><span class="token punctuation">]</span><span class="token punctuation">,</span> data<span class="token punctuation">[</span><span class="token number">7</span><span class="token punctuation">]</span><span class="token punctuation">)</span><span class="token punctuation">,</span> SimpleXYSeries<span class="token punctuation">.</span>ArrayFormat<span class="token punctuation">.</span>Y_VALS_ONLY<span class="token punctuation">)</span><span class="token punctuation">;</span>
    pushArcPlot<span class="token punctuation">.</span><span class="token function">redraw</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre>
<h4 id="ui-modifications">UI Modifications</h4>
<p>To accommodate the new plotting features, the user interface was also refined using <code>ConstraintLayout</code> for a more professional look.</p>
<p>Here’s a snippet showing the updated UI layout:</p>
<pre class=" language-java"><code class="prism  language-java"><span class="token operator">&lt;</span>androidx<span class="token punctuation">.</span>constraintlayout<span class="token punctuation">.</span>widget<span class="token punctuation">.</span>ConstraintLayout xmlns<span class="token operator">:</span>android<span class="token operator">=</span><span class="token string">"http://schemas.android.com/apk/res/android"</span>
    xmlns<span class="token operator">:</span>app<span class="token operator">=</span><span class="token string">"http://schemas.android.com/apk/res-auto"</span>
    xmlns<span class="token operator">:</span>tools<span class="token operator">=</span><span class="token string">"http://schemas.android.com/tools"</span>
    android<span class="token operator">:</span>layout_width<span class="token operator">=</span><span class="token string">"match_parent"</span>
    android<span class="token operator">:</span>layout_height<span class="token operator">=</span><span class="token string">"match_parent"</span>
    tools<span class="token operator">:</span>context<span class="token operator">=</span><span class="token string">".MainActivity"</span><span class="token operator">&gt;</span>

    <span class="token operator">&lt;</span>Button
        android<span class="token operator">:</span>id<span class="token operator">=</span><span class="token string">"@+id/BluetoothButton"</span>
        android<span class="token operator">:</span>layout_width<span class="token operator">=</span><span class="token string">"wrap_content"</span>
        android<span class="token operator">:</span>layout_height<span class="token operator">=</span><span class="token string">"wrap_content"</span>
        android<span class="token operator">:</span>text<span class="token operator">=</span><span class="token string">"Bluetooth"</span>
        app<span class="token operator">:</span>layout_constraintTop_toTopOf<span class="token operator">=</span><span class="token string">"parent"</span>
        app<span class="token operator">:</span>layout_constraintStart_toStartOf<span class="token operator">=</span><span class="token string">"parent"</span>
        app<span class="token operator">:</span>layout_constraintEnd_toEndOf<span class="token operator">=</span><span class="token string">"parent"</span>
        android<span class="token operator">:</span>layout_marginTop<span class="token operator">=</span><span class="token string">"8dp"</span><span class="token operator">/</span><span class="token operator">&gt;</span>

    <span class="token operator">&lt;</span>com<span class="token punctuation">.</span>androidplot<span class="token punctuation">.</span>xy<span class="token punctuation">.</span>XYPlot
        android<span class="token operator">:</span>id<span class="token operator">=</span><span class="token string">"@+id/tangentForcePlot"</span>
        android<span class="token operator">:</span>layout_width<span class="token operator">=</span><span class="token string">"0dp"</span>
        android<span class="token operator">:</span>layout_height<span class="token operator">=</span><span class="token string">"0dp"</span>
        style <span class="token operator">=</span> <span class="token string">"@style/APDefacto.Light"</span>
        app<span class="token operator">:</span>layout_constraintTop_toBottomOf<span class="token operator">=</span><span class="token string">"@id/BluetoothButton"</span>
        app<span class="token operator">:</span>layout_constraintStart_toStartOf<span class="token operator">=</span><span class="token string">"parent"</span>
        app<span class="token operator">:</span>layout_constraintEnd_toEndOf<span class="token operator">=</span><span class="token string">"parent"</span>
        app<span class="token operator">:</span>layout_constraintBottom_toTopOf<span class="token operator">=</span><span class="token string">"@+id/efficiencyPlot"</span>
        app<span class="token operator">:</span>layout_constraintHeight_default<span class="token operator">=</span><span class="token string">"percent"</span>
        app<span class="token operator">:</span>layout_constraintHeight_percent<span class="token operator">=</span><span class="token string">"0.3"</span>
        android<span class="token operator">:</span>layout_marginTop<span class="token operator">=</span><span class="token string">"8dp"</span><span class="token operator">/</span><span class="token operator">&gt;</span>

    <span class="token operator">&lt;</span>com<span class="token punctuation">.</span>androidplot<span class="token punctuation">.</span>xy<span class="token punctuation">.</span>XYPlot
        android<span class="token operator">:</span>id<span class="token operator">=</span><span class="token string">"@+id/efficiencyPlot"</span>
        android<span class="token operator">:</span>layout_width<span class="token operator">=</span><span class="token string">"0dp"</span>
        android<span class="token operator">:</span>layout_height<span class="token operator">=</span><span class="token string">"0dp"</span>
        style <span class="token operator">=</span> <span class="token string">"@style/APDefacto.Light"</span>
        app<span class="token operator">:</span>layout_constraintTop_toBottomOf<span class="token operator">=</span><span class="token string">"@id/tangentForcePlot"</span>
        app<span class="token operator">:</span>layout_constraintStart_toStartOf<span class="token operator">=</span><span class="token string">"parent"</span>
        app<span class="token operator">:</span>layout_constraintEnd_toEndOf<span class="token operator">=</span><span class="token string">"parent"</span>
        app<span class="token operator">:</span>layout_constraintBottom_toTopOf<span class="token operator">=</span><span class="token string">"@+id/pushArcPlot"</span>
        app<span class="token operator">:</span>layout_constraintHeight_default<span class="token operator">=</span><span class="token string">"percent"</span>
        app<span class="token operator">:</span>layout_constraintHeight_percent<span class="token operator">=</span><span class="token string">"0.3"</span><span class="token operator">/</span><span class="token operator">&gt;</span>

    <span class="token operator">&lt;</span>com<span class="token punctuation">.</span>androidplot<span class="token punctuation">.</span>xy<span class="token punctuation">.</span>XYPlot
        android<span class="token operator">:</span>id<span class="token operator">=</span><span class="token string">"@+id/pushArcPlot"</span>
        android<span class="token operator">:</span>layout_width<span class="token operator">=</span><span class="token string">"0dp"</span>
        android<span class="token operator">:</span>layout_height<span class="token operator">=</span><span class="token string">"0dp"</span>
        style <span class="token operator">=</span> <span class="token string">"@style/APDefacto.Light"</span>
        app<span class="token operator">:</span>layout_constraintTop_toBottomOf<span class="token operator">=</span><span class="token string">"@id/efficiencyPlot"</span>
        app<span class="token operator">:</span>layout_constraintStart_toStartOf<span class="token operator">=</span><span class="token string">"parent"</span>
        app<span class="token operator">:</span>layout_constraintEnd_toEndOf<span class="token operator">=</span><span class="token string">"parent"</span>
        app<span class="token operator">:</span>layout_constraintBottom_toBottomOf<span class="token operator">=</span><span class="token string">"parent"</span>
        app<span class="token operator">:</span>layout_constraintHeight_default<span class="token operator">=</span><span class="token string">"percent"</span>
        app<span class="token operator">:</span>layout_constraintHeight_percent<span class="token operator">=</span><span class="token string">"0.3"</span>
        android<span class="token operator">:</span>layout_marginBottom<span class="token operator">=</span><span class="token string">"8dp"</span><span class="token operator">/</span><span class="token operator">&gt;</span>

<span class="token operator">&lt;</span><span class="token operator">/</span>androidx<span class="token punctuation">.</span>constraintlayout<span class="token punctuation">.</span>widget<span class="token punctuation">.</span>ConstraintLayout<span class="token operator">&gt;</span>
</code></pre>
<h3 id="summary-8">Summary</h3>
<p>Today’s tasks focused on integrating the AndroidPlot library to improve the rendering performance and overall look of the UI. By setting up the library and configuring plots for tangent force, efficiency, and push arc, the application now provides smooth and accurate data visualizations. The implementation of dynamic updates ensures real-time data visualization with minimal latency.</p>

